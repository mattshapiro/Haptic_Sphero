package orbotix.uisample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import android.view.View;
import android.widget.CheckBox;
import orbotix.robot.app.ColorPickerActivity;
import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.CollisionDetectedAsyncData;
import orbotix.robot.base.CollisionDetectedAsyncData.CollisionPower;
import orbotix.robot.base.ConfigureCollisionDetectionCommand;
import orbotix.robot.base.DeviceAsyncData;
import orbotix.robot.base.DeviceMessenger;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.base.DeviceMessenger.AsyncDataListener;
import orbotix.robot.sensor.Acceleration;
import orbotix.robot.widgets.ControllerActivity;
import orbotix.robot.widgets.SlideToSleepView;
import orbotix.robot.widgets.calibration.CalibrationView;
import orbotix.robot.widgets.joystick.JoystickView;

public class UiSampleActivity extends ControllerActivity
{
    /**
     * ID to start the StartupActivity for result to connect the Robot
     */
    private final static int STARTUP_ACTIVITY      = 0;

    /**
     * ID to start the ColorPickerActivity for result to select a color
     */
    private final static int COLOR_PICKER_ACTIVITY = 1;
    
    /**
     * Collision threshhold for vibration
     */
    private final static int COLLISION_THRESHOLD = 35;
    
    /**
     * Speed thresshold to avoid vibration during direction changes
     */
    private final static int SPEED_THRESHOLD = 255;
    
    /**
     * Multiplier for turning collision vectors into vibration durations
     */
    private final static int FUDGE_FACTOR = 1;

    /**
     * The Robot to control
     */
    private Robot mRobot;
   
    private CheckBox hapticCheck;
    private boolean isHapticEnabled = true;
    
    //Colors
    private int mRed   = 0xff;
    private int mGreen = 0xff;
    private int mBlue  = 0xff;

    //phone vibrator
    private Vibrator vibrator;
    
    /**
     * SlideToSleepView
     */
    private SlideToSleepView mSlideToSleepView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        //Add the JoystickView as a Controller
        addController((JoystickView)findViewById(R.id.joystick));

        //Add the CalibrationView as a Controller
        addController((CalibrationView)findViewById(R.id.calibration));
        
        //Add the SlideToSleepView
        mSlideToSleepView = (SlideToSleepView)findViewById(R.id.slide_to_sleep);
        
        hapticCheck = (CheckBox)findViewById(R.id.hapticCheck);
        hapticCheck.setChecked(true);
        addController(mSlideToSleepView);
    }

    /**
     * On start, start the StartupActivity to connect to the Robot
     */
    @Override
    protected void onStart() {
        super.onStart();
        
        //Start StartupActivity to connect to Robot
        Intent i = new Intent(this, StartupActivity.class);
        startActivityForResult(i, STARTUP_ACTIVITY);
    }

    private final AsyncDataListener mCollisionListener = new AsyncDataListener() {

		public void onDataReceived(DeviceAsyncData asyncData) {
			if (asyncData instanceof CollisionDetectedAsyncData) {
				if(hapticCheck.isChecked()) {
					final CollisionDetectedAsyncData collisionData = (CollisionDetectedAsyncData) asyncData;
					
					// Update the UI with the collision data
					CollisionPower power = collisionData.getImpactPower();
					long toVibe = (long)(power.x + power.y);
					vibrator.vibrate(toVibe * FUDGE_FACTOR);
				}
			}
		}
	};
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == RESULT_OK){
            if(requestCode == STARTUP_ACTIVITY){

                //Get the connected Robot
                final String robot_id = data.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
                mRobot = RobotProvider.getDefaultProvider().findRobot(robot_id);

                //Set connected Robot to the Controllers
                setRobot(mRobot);
                
                // Start streaming collision detection data
    			//// First register a listener to process the data
    			DeviceMessenger.getInstance().addAsyncDataListener(mRobot,
    					mCollisionListener);

    			//// Now send a command to enable streaming collisions
    			//// 
    			ConfigureCollisionDetectionCommand.sendCommand(mRobot, ConfigureCollisionDetectionCommand.DEFAULT_DETECTION_METHOD,
    					COLLISION_THRESHOLD, COLLISION_THRESHOLD, SPEED_THRESHOLD, SPEED_THRESHOLD, 0);
                
            }else if(requestCode == COLOR_PICKER_ACTIVITY){
                
                if(mRobot != null){
                    //Get the colors
                    mRed   = data.getIntExtra(ColorPickerActivity.EXTRA_COLOR_RED, 0xff);
                    mGreen = data.getIntExtra(ColorPickerActivity.EXTRA_COLOR_GREEN, 0xff);
                    mBlue  = data.getIntExtra(ColorPickerActivity.EXTRA_COLOR_BLUE, 0xff);

                    //Set the color
                    RGBLEDOutputCommand.sendCommand(mRobot, mRed, mGreen, mBlue);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Disconnect Robots on stop
        if(mRobot != null){
            RobotProvider.getDefaultProvider().disconnectControlledRobots();
        }
        
        // Assume that collision detection is configured and disable it.
     	ConfigureCollisionDetectionCommand.sendCommand(mRobot, ConfigureCollisionDetectionCommand.DISABLE_DETECTION_METHOD, 0, 0, 0, 0, 0);
     		
 		// Remove async data listener
 		DeviceMessenger.getInstance().removeAsyncDataListener(mRobot, mCollisionListener);
 		
 		// Disconnect from the robot.
 		RobotProvider.getDefaultProvider().removeAllControls();
    }

    /**
     * When the user clicks on the "Sleep" button, show the SlideToSleepView
     * @param v The Button clicked
     */
    public void onSleepClick(View v){
        mSlideToSleepView.show();
    }

    /**
     * When the user clicks the "Color" button, show the ColorPickerActivity
     * @param v The Button clicked
     */
    public void onColorClick(View v){
        
        Intent i = new Intent(this, ColorPickerActivity.class);

        //Tell the ColorPickerActivity which color to have the cursor on.
        i.putExtra(ColorPickerActivity.EXTRA_COLOR_RED, mRed);
        i.putExtra(ColorPickerActivity.EXTRA_COLOR_GREEN, mGreen);
        i.putExtra(ColorPickerActivity.EXTRA_COLOR_BLUE, mBlue);
        
        startActivityForResult(i, COLOR_PICKER_ACTIVITY);
    }
}
