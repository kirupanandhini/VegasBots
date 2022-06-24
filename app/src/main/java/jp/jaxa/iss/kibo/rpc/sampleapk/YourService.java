package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.util.Log;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

import org.opencv.core.Mat;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void runPlan1(){
        Log.i(TAG, "start mission");
        // the mission starts
        boolean result = api.startMission();

        // move to a point
        Point point = new Point(10.71000f, -7.70000f, 4.48000f);
        Quaternion quaternion = new Quaternion(0f, 0.707f, 0f, 0.707f);
        Result r = api.moveTo(point, quaternion, false);

        api.reportPoint1Arrival();

        final int LOOP_MAX = 5;
        // check result and loop while moveTo api is not succeeded
        int loopCounter = 0;
        while (!r.hasSucceeded() && loopCounter < LOOP_MAX) {
            // retry
            r = api.moveTo(point, quaternion, true);
            api.laserControl(true);
            api.takeTarget1Snapshot();
            api.laserControl(false);
            ++loopCounter;
        }
        // report point1 arrival

        Mat image = api.getMatNavCam();

        // save the image
        api.saveMatImage(image, "file_name.png");

        // irradiate the laser
        api.laserControl(true);

        // take target1 snapshots
        api.takeTarget1Snapshot();

        // turn the laser off
        api.laserControl(false);

        //move to point 2
        Point point2 = new Point(11.35000f, -8.50000f, 4.58000f);
        Quaternion quaternion2 = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point2, quaternion2, false);

        point2 = new Point(11.35000f, -9.50000f, 4.58000f);
        quaternion2 = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point2, quaternion2, false);

        point2 = new Point(11.27460f, -9.92284f, 5.29881f);
        quaternion2 = new Quaternion(0f, 0f, -0.707f, 0.707f);
        r = api.moveTo(point2, quaternion2, false);
        //report point2 arrival
        api.reportPoint1Arrival();
        api.getRobotKinematics();

        loopCounter = 0;
        while (!r.hasSucceeded() && loopCounter < 10) {
            // retry
            r = api.moveTo(point, quaternion, true);
            api.laserControl(true);
            api.takeTarget2Snapshot();
            api.laserControl(false);
            ++loopCounter;
        }

        // get a camera image
        image = api.getMatNavCam();

        // save the image
        api.saveMatImage(image, "file_nam.png");

        // irradiate the laser
        api.laserControl(true);

        // take target1 snapshots
        api.takeTarget2Snapshot();

        // turn the laser off
        api.laserControl(false);

        /* ******************************************** */
        /* write your own code and repair the air leak! */
        /* ******************************************** */

        // send mission completion

        Point finalPoint = new Point(10.77460f, -9.42000f, 5.29881f);
        Quaternion finalQuaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(finalPoint, finalQuaternion, false);

        finalPoint = new Point(10.77460f, -8.92000f, 5.29881f);
        finalQuaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(finalPoint, finalQuaternion, false);

        finalPoint = new Point(11.27460f, -7.89178f, 4.96538f);
        finalQuaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(finalPoint, finalQuaternion, false);


        api.reportMissionCompletion();
    }
    //hello

    @Override
    protected void runPlan2(){
        // write here your plan 2
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    // You can add your method
    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){

        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);

        api.moveTo(point, quaternion, true);
    }

    private void relativeMoveToWrapper(double pos_x, double pos_y, double pos_z,
                                       double qua_x, double qua_y, double qua_z,
                                       double qua_w) {

        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float) qua_x, (float) qua_y,
                (float) qua_z, (float) qua_w);

        api.relativeMoveTo(point, quaternion, true);
    }

}
