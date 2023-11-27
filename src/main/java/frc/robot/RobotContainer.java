// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveTeamConstants;
import frc.robot.subsystems.swerve.SwerveSubsystem;

import java.sql.Time;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final CommandXboxController m_driverController =
      new CommandXboxController(DriveTeamConstants.kDriverControllerPort);

  private final SlewRateLimiter m_driverXLimiter = new SlewRateLimiter(10);
  private final SlewRateLimiter m_driverYLimiter = new SlewRateLimiter(10);
  private final SlewRateLimiter m_driverRotLimiter = new SlewRateLimiter(1);

  private final SwerveSubsystem m_swerve;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // instantiate swerve
    m_swerve = new SwerveSubsystem();

    m_swerve.setDefaultCommand(
      m_swerve.getDriveWithJoystickCommand(
        () -> m_driverXLimiter.calculate(m_driverController.getLeftX()), // l/r
        () -> m_driverYLimiter.calculate(-m_driverController.getLeftY()), // f/b
        () -> m_driverRotLimiter.calculate(-m_driverController.getRightX()), // rot
        () -> Constants.SwerveConstants.kFieldRelative) // field relative
    );

    
    // Configure the controller bindings
    configureButtonBindings();
  }

  /** 
   * Configure button bindings for controllers (axis bindings may not be handled by this method)
  */
  private void configureButtonBindings() {
    m_driverController.a().onTrue(m_swerve.getAlignWheelCommand());
    m_driverController.x().onTrue(m_swerve.getDriveWithTrajectoryCommand(() -> Timer.getFPGATimestamp(), Common.currentAutonTrajectory));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // public Command getAutonomousCommand() {
    
  // }
}
