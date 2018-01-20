package drawing.drawing.messaging;

/**
 * Created by leo on 18/01/18.
 */


public interface MessagingInterface {
    void show(CustomProgressDialog.DialogType type, String line1, String line2);
    void show(CustomProgressDialog.DialogType type, String line1);
    void show(String line1, String line2);
    void show(String line1);
    void dismiss();
}