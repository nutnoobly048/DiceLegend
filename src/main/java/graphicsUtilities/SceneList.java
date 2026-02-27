package graphicsUtilities;


import misc.Player;

public enum SceneList {
    MENU {
        @Override
        public Scene create() {
            Scene scene = new Scene();
            return scene;
        }
    },
    LEVEL_1 {
        @Override
        public Scene create() {
            Scene scene = new Scene();
            scene.spawnObjectAt(new Player("licoCake144", 100, 100), 100, 100);
            return scene;
        }
    },
    LEVEL_2 {
        @Override
        public Scene create() {
            Scene scene = new Scene();
            scene.spawnObjectAt(new Player("licoCake144", 400, 400), 400, 400);
            return scene;
        }
    };

    public abstract Scene create();
}