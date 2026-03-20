# DiceLegend

A multiplayer board game with snakes and ladders mechanics, built with Java Swing and MQTT networking.

## Project Overview

DiceLegend is a turn-based multiplayer board game supporting 2-4 players. Players compete to reach the final tile first by rolling dice and navigating through snakes and ladders. The game features a host-client architecture using MQTT for real-time network synchronization.

**Build Tool:** Maven  
**Framework:** Java Swing  
**Networking:** HiveMQ MQTT Client

## Project Structure

```
src/main/java/
├── animation/           # Tween and Timeline animation system
├── graphicsUtilities/    # Scene, rendering, image loading
├── objectClass/          # Game objects (Board, GameObject, Pawn, etc.)
├── Gameplay/             # Core game logic (GameState, Cell, Board)
├── scene/                # Game scenes (Menu, Lobby, Game)
├── service/              # Game loop, networking, input handling
├── ServiceInterface/     # Interfaces and enums
├── Item/                 # Item system (abstract)
├── Event/                # Event system (abstract)
└── OtherUtilities/       # Utility classes
```

## Key Classes & Interfaces

### Core Engine

#### `RunService` (service/RunService.java)
The **game loop engine** - the heart of the game that runs approximately 60 FPS.

**Key Responsibilities:**
- Game loop with delta-time calculation for frame-independent movement
- Process registration (objects implementing `ProcessByRunService`)
- MQTT message queue processing (Intent & Result queues)
- Scene management integration
- Console debugger for testing

**Key Methods:**
- `start()` - Initializes the game loop on a separate thread
- `addProcess(ProcessByRunService)` - Register objects to receive `OnUpdate()` calls
- `addRunnable(Consumer<Double>)` - Register functional callbacks per frame

#### `ProcessByRunService` (ServiceInterface/ProcessByRunService.java)
```java
public interface ProcessByRunService {
    void OnUpdate(double deltatime);
}
```
Interface for objects that need to update every frame. Implemented by `GameObject`, `Tween`, and `Timeline`.

---

### Graphics System

#### `Scene` (graphicsUtilities/Scene.java)
A `JPanel` subclass that acts as a **stage** containing all visual game objects.

**Key Properties:**
- `drawList` - ArrayList of GameObjects sorted by z-depth
- `currentSceneObject` - HashMap for quick object lookup by networkId
- Background image support
- Scene enter/exit callbacks with transition hooks

**Key Methods:**
- `spawnObjectAt(GameObject, x, y)` - Add object to scene at position
- `getObjectByID(String)` - Find object by networkId
- `setOnSceneEnter(Runnable)` / `setOnSceneExited(Runnable)` - Transition callbacks

#### `GameObject` (objectClass/GameObject.java)
**Base class** for all game entities. Everything visible in the game extends this.

**Key Properties:**
| Property | Type | Purpose |
|----------|------|---------|
| `x, y` | int | Position in pixels |
| `z` | int | Rendering depth (lower = drawn first) |
| `isActive, isVisible` | boolean | Visibility flags |
| `networkId` | String | Unique identifier for networking |
| `sprite` | AnimatedSprite | Visual representation |
| `currentScene` | Scene | Reference to parent scene |

**Key Methods:**
- `OnUpdate(double deltaTime)` - Called every frame
- `moveTo(int x, int y)` - Teleport to position
- `hasSprite()` / `getSprite()` - Sprite management

#### `AnimatedSprite` (objectClass/AnimatedSprite.java)
Handles sprite sheet animation with frame-based playback.

**Features:**
- Frame-based animation (horizontal sprite sheet)
- Loop/rollback playback modes
- FPS-based animation speed
- Offset positioning (for anchor points)

#### `Drawable` (ServiceInterface/Drawable.java)
```java
public interface Drawable {
    void draw(Graphics2D g2d);
}
```
Interface for objects that can render themselves. Implemented by `AnimatedSprite`.

---

### Animation System

#### `Tween` (animation/Tween.java)
**Property interpolation** - animates numeric values over time.

**Usage:**
```java
new Tween(player, TweenProperty.X, 100, 500, 2.0).start();
// Animates X from 100 to 500 over 2 seconds
```

**TweenProperty enum:** `X`, `Y`, `ALPHA`, `SCALE`

#### `Timeline` (animation/Timeline.java)
**Sequences multiple Tweens** with delays for complex animations.

**Usage:**
```java
Timeline tl = new Timeline();
tl.add(new Tween(obj, TweenProperty.X, 0, 100, 1), 0);      // At time 0
tl.add(new Tween(obj, TweenProperty.Y, 0, 200, 1), 1);      // At time 1
tl.play();
```

---

### Scene Management

#### `SceneUtilities` (graphicsUtilities/SceneUtilities.java)
Static utility for switching between scenes with **transition animations**.

**Key Methods:**
- `changeSceneTo(Scene)` - Switch scenes with exit/enter transitions
- `getCurrentGameScene()` - Get the active scene

#### Scene List (Gameplay/SceneList.java)
Factory for creating game scenes:
- `mainMenu` - Static main menu scene
- `lobbyScene` - Static lobby scene
- `buildMysteriousJungle()` - Creates a game board scene

#### Available Scenes
| Scene | Purpose |
|-------|---------|
| `MainMenuScene` | Title screen with Create/Join buttons, particle effects |
| `LoadingScene` | Network connection screen |
| `LobbyScene` | Pre-game lobby, player list, start button |
| `MysteriousJungleScene` | The main game board with pawns |

---

### Networking System

#### `MQTTService` (ServiceInterface/MQTTService.java)
Wrapper around HiveMQ MQTT client for **real-time communication**.

**Key Methods:**
| Method | Purpose |
|--------|---------|
| `connect()` | Connect to MQTT broker |
| `connectWithWill()` | Connect with Last Will (host disconnect detection) |
| `publish(topic, msg)` | Send a message |
| `subscribe(topic, handler)` | Receive messages |
| `publishRetained()` | Retained publish (for state sync) |
| `clearRetained()` | Clear retained messages (cleanup) |

**Default Broker:** `broker.hivemq.com:1883`

#### Message Protocol

The game uses a **pub/sub message format** via MQTT topics:

```
Topic: DiceLegend/{lobbyName}/
├── /Intents     - Client actions (host processes)
├── /Results     - Server responses (all clients receive)
└── /room_state  - Room status (ACTIVE/HOST_DISCONNECTED)
```

**Message Format:**
```
INTENT:{playerID}:{action}:{param0}:{param1}:...
RESULT:{targetID}:{action}:{param0}:{param1}:...
```

#### `CommandHandler` (service/CommandHandler.java)
Central **message router** that handles all network communication.

**Network Flow:**
```
Client Action → sentIntent() → IntentQueue → handleIntent() (host only)
                                                          ↓
Host Decision → broadcastResult() → ResultQueue → handleResult() (all clients)
                                                          ↓
                                                     GameState.handleEvent()
```

**Intent Actions (client → host):**
| Action | Description |
|--------|-------------|
| `JOIN_GAME` | Player enters lobby |
| `LEAVE_GAME` | Player exits lobby |
| `START_GAME` | Host starts the game |
| `CONTINUE` | Player ready signal |
| `ROLLEVENT` | Player rolls dice |

**Result Actions (host → all):**
| Action | Description |
|--------|-------------|
| `PLAYER_JOINED` | Sync new player |
| `PLAYER_LEFT` | Remove player |
| `GAME_STARTED` | Begin game |
| `DICE_ROLLED` | Roll result |
| `MOVETO` | Move pawn command |
| `CHANGESCENETO` | Switch scene |
| `PLAYER_SPRITE_CHANGED` | Sprite update |

---

### Game State Management

#### `GameState` (Gameplay/GameState.java)
**Singleton** (`currentGame`) that manages all game state and turn logic.

**Key State:**
| Property | Type | Description |
|----------|------|-------------|
| `currentPhase` | GamePhase | Current game phase |
| `currentPlayerTurnId` | String | Whose turn it is |
| `currentPlayerTurnIndex` | int | Turn order index |
| `lastRollResult` | int | Last dice roll |
| `allPlayers` | HashMap | All players by networkId |
| `spawnedCharacter` | HashMap | All pawns by networkId |
| `gameBoard` | Board | The current board |

#### Game Phases (GamePhase enum)
```
WAIT_FOR_PLAYERS → WAIT_FOR_READY → TURN_START → WAIT_FOR_ROLL
                                                       ↓
                                                   (roll dice)
                                                       ↓
                    ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ←
                        EXECUTE_MOVEMENT → CHECK_TILE
                               ↓
                          TURN_END → (next player)
```

#### Phase Handlers
Each phase has a `handle{Phase}()` method that processes events and transitions.

---

### Player & Pawn Characters

#### `Player` (misc/Player.java)
Represents the **human player** - identity and turn state.

**Key Properties:**
| Property | Purpose |
|----------|---------|
| `networkID` | Unique identifier (matches MQTT client ID) |
| `name` | Display name |
| `localSpriteName` | Player's chosen sprite |
| `isReadyToPlay` | Lobby ready state |
| `isReadyToContinue` | Phase ready state |
| `openForNetworkInput` | Turn lock (only active player can act) |
| `remainingSkipTurns` | Penalty tracking |

**Static Singleton:** `Player.localPlayer` - reference to the local machine's player

#### `PawnCharacter` (misc/PawnCharacter.java)
Extends `GameObject` - the **visual game piece** on the board.

**Key Properties:**
| Property | Purpose |
|----------|---------|
| `currentTileIndex` | Position on board (0 to board size-1) |
| `moveQueue` | Queued movement targets |
| `isAnimating` | Movement animation state |

**Key Methods:**
- `moveToTileIndex(int)` - Queue movement to tile
- `animateStepByStep(int)` - Walk animation (short moves)
- `animateDirectJump(int)` - Jump animation (long moves, snakes/ladders)

**SLOT_OFFSETS:** Pixel offsets when multiple pawns share a tile

**Connection to Player:** Both share the same `networkId` - `GameState` links them via:
```java
allPlayers.get(networkId)           // Player "soul"
spawnedCharacter.get(networkId)     // Pawn "body"
```

---

### Board System

#### `Board` (objectClass/Board.java)
Contains the board layout and tile data.

**Static Board Data:**
- `coordinatesMysteriousJungle` - Pixel positions for each tile
- `destinationMysteriousJungle` - Snake/ladder mappings `[from, to]`

**Key Methods:**
- `getPositionFromIndex(int)` → `int[]` of `{x, y}`
- `getAttributeFromIndex(int)` → `CellAttribute`
- `getDestinationFromIndex(int)` → Final destination (for snakes/ladders)

#### `Cell` (Gameplay/Cell.java)
Represents a single tile on the board.

**Properties:**
- `index` - Tile position
- `xPos, yPos` - Pixel coordinates
- `attributes` - `CellAttribute` enum
- `destinationIndex` - Target for snakes/ladders (-1 = none)

#### `CellAttribute` (ServiceInterface/CellAttribute.java)
```java
NORMAL_TILE, WIN_TILE, START_TILE,
EVENT_TILE, ITEM_TILE,
WATER_TILE, WHEAT_TILE,
ABYSS_TILE,
SNAKE_TILE, LADDER_TILE
```

---

### Items & Events (Abstract Systems)

#### `Item` (Item/Item.java)
Abstract base for usable items during a player's turn.

```java
public abstract class Item {
    String itemId, itemName;
    
    public final void execute(Player user, Player target, GameState state) {
        doImmediateAction(user, target, state);
        broadcastResult(user, target, state);
    }
    
    public abstract void doImmediateAction(Player user, Player target, GameState state);
    public abstract void broadcastResult(Player user, Player target, GameState state);
    public abstract String getCardUIName();
}
```

#### `Event` (Event/Event.java)
Abstract base for tile events.

```java
public abstract class Event {
    String eventId, eventName;
    
    public final void execute(GameState game) {
        doVisual(game);
        doImmediateAction(game);
        broadcastResult(game);
    }
    
    public abstract void doVisual(GameState game);
    public abstract void doImmediateAction(GameState game);
    public abstract void broadcastResult(GameState game);
    public abstract String getEventVisualName();
}
```

---

### UI Components

#### `GameButton` (objectClass/GameButton.java)
Custom styled button extending `JButton`.

**Features:**
- Image-based styling (normal/hover states)
- Custom event callbacks
- Styled text rendering

#### `ImagePreload` (graphicsUtilities/ImagePreload.java)
Preloads all images from `src/main/Image/` into a HashMap for fast access.

**Usage:** `ImagePreload.get("filename.png")`

---

### Input Handling

#### `UserInput` (service/UserInput.java)
Keyboard input manager with **just-pressed** detection.

**Key Methods:**
| Method | Purpose |
|--------|---------|
| `isKeyHold(int keyCode)` | True while key is held |
| `isKeyJustPressed(int keyCode)` | True only on first frame of press |
| `isKeyJustReleased(int keyCode)` | True only on first frame of release |

**Current bindings:**
- `SPACE` - Roll dice

---

## Game Flow

### 1. Startup
```
MainGame.main()
    → RunService.start()
    → SceneUtilities.changeSceneTo(MainMenuScene)
```

### 2. Lobby (Host)
```
Create Room → LoadingScene (isHost=true)
    → MQTT connectWithWill()
    → Publish retained "ACTIVE"
    → Subscribe to /Intents
    → Wait for players via JOIN_GAME intents
```

### 3. Lobby (Client)
```
Join Room → LoadingScene (isHost=false)
    → MQTT connect()
    → Subscribe to /room_state
    → Wait for "ACTIVE" signal
    → Subscribe to /Intents and /Results
    → Send JOIN_GAME intent
```

### 4. Game Start
```
Host clicks START
    → CHANGESCENETO broadcast
    → GAME_STARTED broadcast
    → GameState initializes Board
    → Change to MysteriousJungleScene
    → SpawnAllPawns() creates pawns
    → TURN_START phase
```

### 5. Turn Sequence
```
TURN_START
    → beginTurnForPlayer(currentPlayerTurnId)
    → Only that player can send intents (openForNetworkInput=true)
    → WAIT_FOR_ROLL

Player presses SPACE or clicks Roll
    → ROLLEVENT intent → DICE_ROLLED result
    → Calculate destination
    → MOVETO broadcast (and second MOVETO for snakes/ladders)
    → EXECUTE_MOVEMENT

All clients animate pawn movement
    → When animation completes → CONTINUE intent
    → When all ready → CHECK_TILE
    → Check cell attribute → apply effects
    → advanceToNextPlayer()
```

---

## Adding New Content

### Add a New Map
1. Define tile coordinates in `Board`:
   ```java
   public static int[][] coordinatesMyMap = { ... };
   public static int[][] destinationMyMap = { ... }; // snakes/ladders
   ```
2. Add case in `GameState.handleWaitForPlayers()`:
   ```java
   case "myMap" -> gameBoard = new Board(Board.coordinatesMyMap, Board.destinationMyMap);
   ```
3. Add factory method in `SceneList`:
   ```java
   public static Scene buildMyMap() { return new MyMapScene(); }
   ```

### Add a New Scene
1. Extend `Scene` class
2. Implement `setupObjects()` for game objects
3. Implement `setupButtons()` for UI
4. Add scene creation to `SceneList`
5. Call `SceneUtilities.changeSceneTo()` to switch

### Add a New Item
1. Extend `Item` abstract class
2. Implement `doImmediateAction()` - apply effect
3. Implement `broadcastResult()` - sync to other clients
4. Implement `getCardUIName()` - UI display name

### Add Tween Animation
```java
new Tween(target, TweenProperty.X, from, to, duration)
    .OnComplete(() -> { /* callback */ })
    .start();
```

---

## Key Design Patterns

1. **Singleton** - `RunService`, `Player.localPlayer`, `GameState.currentGame`
2. **Observer** - `ProcessByRunService` registration/callback pattern
3. **State Machine** - `GamePhase` enum with phase handlers
4. **Command Pattern** - Intent/Result message routing
5. **Factory Method** - `SceneList` scene builders
6. **Template Method** - `Item.execute()`, `Event.execute()` with abstract hooks

---

## Configuration

| Setting | Location | Default |
|---------|----------|---------|
| MQTT Broker | `MQTTService.java:25` | `broker.hivemq.com:1883` |
| Screen Size | `Scene.java:27` | `1920x1080` |
| Animation FPS | `AnimatedSprite` | Configurable per sprite |
| Board Map | `GameState.java:21` | `mysteriousJungle` |

---

Generated By AI
