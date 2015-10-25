package communication;

import DBmanager.SchemaDevice;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by anders on 10/24/15.
 */
public abstract class CommandHandler {

    Logger iLog = LogManager.getLogger(CommandHandler.class);

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-mm-dd HH:mm:ss");

    private static TelldusInterface telldusInterface;

    private void ExecuteCommand(SchemaDevice device){
        if (telldusInterface == null)
            telldusInterface = Initialize.getTelldusInterface();

        if (device.getAction() == 0){
            iLog.info("Action was 0, trying to turn off device + " + device.getDeviceID());
            telldusInterface.tdTurnOff(device.getDeviceID());
        }
        if (device.getAction()== 1){
            iLog.info("Action was 1, trying to turn on device + " + device.getDeviceID());
            telldusInterface.tdTurnOn(device.getDeviceID());
        }


        updateConfiguration(device);

    }

    protected abstract void updateConfiguration(SchemaDevice device);



    public void HandleConfiguredDevice(SchemaDevice device){

        DateTime configuredTimePoint = device.getTimePoint();
        DateTime lastUpdatedAt = device.getUpdatedAt();

        if (configuredTimePoint.getMinuteOfDay() == DateTime.now().getMinuteOfDay()){

            if (device.getUpdatedAt() != null) {
                int secDif = device.getUpdatedAt().getSecondOfDay() - DateTime.now().getSecondOfDay();
                if (secDif < 60)
                    return;
            }

            ExecuteCommand(device);
        }

    }
}
