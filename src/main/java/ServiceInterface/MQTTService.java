import java.nio.charset.StandardCharsets;
import java.util.UUID;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.*;
import com.hivemq.client.mqtt.datatypes.MqttQos;

public class MQTTService {

    private Mqtt5AsyncClient client;
    public String clientId;

    // ส่วนหลักของการเชื่อมต่อกับ MQTT Broker
    public MQTTService() {

        clientId = UUID.randomUUID().toString();

        client = MqttClient.builder()
                .useMqttVersion5()
                .identifier(clientId)
                .serverHost("broker.hivemq.com")
                .serverPort(1883)
                .buildAsync();
    }

    // ส่วนของการเชื่อมต่อกับ MQTT Broker ของ Player
    public void connect() {
        client.connect().join();
        System.out.println("Connected to MQTT Broker");
    }

    // ส่วนของการเชื่อมต่อกับ MQTT Broker ของ Host ถ้า Host หลุด/ปิด ก็จะแจ้ง Player
    public void connectWithWill(String willTopic, String willMessage) {
        client.connectWith()
                .willPublish()
                .topic(willTopic)
                .payload(willMessage.getBytes(StandardCharsets.UTF_8))
                .qos(MqttQos.AT_LEAST_ONCE)
                .retain(false)
                .applyWillPublish()
                .send()
                .join();
        System.out.println("Host connected to MQTT Broker");
    }

    // ส่วนของการ disconnect เมื่อต้องการออกจากห้อง/หลุดออกจากห้อง
    public void disconnect() {
        client.disconnect().join();
    }

    // กำหนด Default interface ของ Message ที่ใช้กับ subscribe
    public interface MessageHandler {
        void onMessage(String topic, String message);
    }

    // การ subscribe / การส่งข้อมูล แบบ Normal
    public void subscribe(String topic, MessageHandler handler) {

        client.subscribeWith()
                .topicFilter(topic)
                .callback(publish -> {
                    String msg = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                    handler.onMessage(publish.getTopic().toString(), msg);
                })
                .send()
                .join();
    }

    // การ publish / การรับข้อมูล แบบ Normal
    public void publish(String topic, String message) {

        client.publishWith()
                .topic(topic)
                .payload(message.getBytes(StandardCharsets.UTF_8))
                .send();
    }

    // การ publish ให้ Player ที่เข้ามาใหม่โดยเอา Message ล่าสุดส่ง
    public void publishRetained(String topic, String message) {

        client.publishWith()
                .topic(topic)
                .payload(message.getBytes(StandardCharsets.UTF_8))
                .retain(true)
                .qos(MqttQos.AT_LEAST_ONCE)
                .send()
                .join();
    }

    /*
     * การ publish โดยให้รอจนสำเร็จ ถ้าไม่มีเมื่อจบเกมส่ง Result จบแล้วอาจจะมีบางคน
     * Disconnect ไปก่อน
     */
    public void publishBlocking(String topic, String message) {

        client.publishWith()
                .topic(topic)
                .payload(message.getBytes(StandardCharsets.UTF_8))
                .qos(MqttQos.AT_LEAST_ONCE)
                .send()
                .join();
    }

    // การ Reset Message ที่เคยเก็บไว้เมื่อต้องการ Re-game
    public void clearRetained(String topic) {

        client.publishWith()
                .topic(topic)
                .payload(new byte[0])
                .retain(true)
                .send()
                .join();
    }

}
