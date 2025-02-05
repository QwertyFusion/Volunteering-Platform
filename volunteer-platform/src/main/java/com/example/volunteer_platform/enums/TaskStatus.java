package com.example.volunteer_platform.enums;

/**
 * TaskStatus enum represents the various statuses a task can have in the system.
 */
public enum TaskStatus {
    AVAILABLE,              // Task is available for application
    FILLED,                 // No more volunteers needed for the task. [Will be implemented in future]
    APPLICATION_ENDED,      // Task is no longer available for application; application date is over
    ENDED,                  // Task is no longer available for application; event date is over
    CANCELLED               // Task has been cancelled by the organization; cannot be applied for any longer
}