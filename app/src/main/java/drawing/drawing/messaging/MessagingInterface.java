package drawing.drawing.messaging;

/**
 * VectorDrawing for FretX
 * Created by pandor on 18/01/18 20:07.
 */


public interface MessagingInterface {
    void show(CustomProgressDialog.DialogType type, String line1, String line2);
    void show(CustomProgressDialog.DialogType type, String line1);
    void show(String line1, String line2);
    void show(String line1);
    void allowCancellation();
    void dismiss();
}