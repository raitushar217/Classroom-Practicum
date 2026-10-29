package com.example.reater.data

import com.example.reater.models.Assignment

object LocalAssignmentRepository {
    private val assignments = mutableListOf(
        Assignment(
            "1",
            "IT-E",
            "IT3126",
            "25/05/2024",
            "Devesh Choudhary",
            "Late submissions will be not be entertained at any cost,so submit by the due date",
            "Process Synocronisation"
        ),
        Assignment(
            "202",
            "IT-E",
            "IT2126",
            "25/07/2024",
            "Veena Khandelwal",
            "Late submissions will be not be entertained at any cost,so submit by the due date",
            "Decision Tree Problems"
        )
    )

    fun getAssignments(): List<Assignment> {
        return assignments.toList()
    }

    fun addAssignment(assignment: Assignment) {
        assignments.add(0, assignment) // Add to the top of the list
    }
}
