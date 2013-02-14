package com.edinarobotics.zed.commands;

import com.edinarobotics.utils.gamepad.FilterSet;
import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.gamepad.GamepadResult;
import com.edinarobotics.utils.gamepad.filters.DeadzoneFilter;
import com.edinarobotics.utils.gamepad.filters.ScalingFilter;
import com.edinarobotics.zed.Components;
import com.edinarobotics.zed.subsystems.Drivetrain;
import com.edinarobotics.zed.subsystems.DrivetrainRotation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This {@link Command} allows a gamepad to control the {@link Drivetrain}.
 * It gets the current values from the gamepad and sends them to the drivetrain.
 */
public class GamepadDriveRotationCommand extends Command{
    private static final String COMMAND_NAME = "GamepadDriveRotation";
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private FilterSet filters;
    private DrivetrainRotation drivetrainRotation;
    
    private static double ZERO_THRESHOLD = 0.05;
    
    /**
     * Construct a new GamepadDriveCommand using the given gamepad, filters
     * and drivetrain.
     * @param gamepad The Gamepad object to read for control values.
     * @param gamepad2 The second Gamepad object to read for control values.
     * @param filters The set of filters to use when filtering gamepad output.
     */
    public GamepadDriveRotationCommand(Gamepad gamepad, Gamepad gamepad2, FilterSet filters){
        super(COMMAND_NAME);
        this.gamepad1 = gamepad;
        this.gamepad2 = gamepad2;
        this.filters = filters;
        this.drivetrainRotation = Components.getInstance().drivetrainRotation;
        requires(drivetrainRotation);
    }
    
    /**
     * Constructs a new GamepadDriveCommand using the given gamepad and
     * drivetrain. This command will use a default set of filters.
     * @param gamepad The Gamepad object to read for control values.
     * @param gamepad2 The second gamepad object to read for control values.
     */
    public GamepadDriveRotationCommand(Gamepad gamepad, Gamepad gamepad2){
        super(COMMAND_NAME);
        filters = new FilterSet();
        filters.addFilter(new DeadzoneFilter(0.15));
        filters.addFilter(new ScalingFilter());
        this.gamepad1 = gamepad;
        this.gamepad2 = gamepad2;
        this.drivetrainRotation = Components.getInstance().drivetrainRotation;
        requires(drivetrainRotation);
    }

    protected void initialize() {
        
    }

    /**
     * Submits values from the given {@code gamepad} to the given
     * {@code drivetrain}.
     */
    protected void execute() {
        GamepadResult gamepad1State = filters.filter(gamepad1.getJoysticks());
        GamepadResult gamepad2State = filters.filter(gamepad2.getJoysticks());
        //Precedence: gamepad then gamepad2
        double gamepad1Value = gamepad1State.getRightX();
        double gamepad2Value = gamepad2State.getRightX();
        if(!isZero(gamepad1Value)){
            drivetrainRotation.mecanumPolarRotate(gamepad1Value);
        }
        else{
            drivetrainRotation.mecanumPolarRotate(gamepad2Value);
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        
    }

    protected void interrupted() {
        
    }
    
    private boolean isZero(double value){
       return Math.abs(value) <= ZERO_THRESHOLD;
    }
}
