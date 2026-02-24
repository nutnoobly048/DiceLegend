package ServiceInterface;

public interface ProcessByRunService {
    void OnCreate();
    void OnUpdate(double deltatime);
    void OnLateUpdate();

    void OnRemoved();
}
