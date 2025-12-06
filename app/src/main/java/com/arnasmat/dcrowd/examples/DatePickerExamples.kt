package com.arnasmat.dcrowd.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arnasmat.dcrowd.util.DatePickerUtils

/**
 * EXAMPLE FILE: Comprehensive examples of date picker usage and Unix timestamp conversion
 *
 * This file demonstrates various use cases for date pickers in Android with Unix timestamp conversion.
 * These examples are ready to use in your project.
 */

// ============================================================================
// Example 1: Basic Date Selection with Unix Timestamp
// ============================================================================

@Composable
fun Example1_BasicDatePicker() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Store date as Unix timestamp (seconds since epoch)
    var selectedDate by remember { mutableStateOf(DatePickerUtils.getCurrentUnixTimestamp()) }
    var displayText by remember { mutableStateOf("No date selected") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selected Unix Timestamp: $selectedDate")
        Text("Formatted Date: ${DatePickerUtils.formatUnixTimestamp(selectedDate)}")

        Button(onClick = {
            DatePickerUtils.showDatePicker(
                context = context,
                initialUnixTimestamp = selectedDate,
                onDateSelected = { newTimestamp ->
                    selectedDate = newTimestamp
                    displayText = "Selected: ${DatePickerUtils.formatUnixTimestamp(newTimestamp)}"
                }
            )
        }) {
            Text("Pick Date")
        }

        Text(displayText)
    }
}

// ============================================================================
// Example 2: Date Picker with Constraints (Min/Max Date)
// ============================================================================

@Composable
fun Example2_DatePickerWithConstraints() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Project deadline must be between tomorrow and 1 year from now
    val minDate = DatePickerUtils.addDays(DatePickerUtils.getCurrentUnixTimestamp(), 1)
    val maxDate = DatePickerUtils.addDays(DatePickerUtils.getCurrentUnixTimestamp(), 365)

    var deadline by remember { mutableStateOf(minDate) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Project Deadline")
        Text("Must be between tomorrow and 1 year from now")

        OutlinedTextField(
            value = DatePickerUtils.formatUnixTimestamp(deadline),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerUtils.showDatePicker(
                        context = context,
                        initialUnixTimestamp = deadline,
                        minDate = minDate,
                        maxDate = maxDate,
                        onDateSelected = { deadline = it }
                    )
                }) {
                    Text("📅")
                }
            }
        )

        Text("Unix Timestamp: $deadline (will be sent to backend)")
    }
}

// ============================================================================
// Example 3: Multiple Milestones with Deadlines
// ============================================================================

data class MilestoneData(
    val id: Int,
    val goalEth: String,
    val deadlineUnixTimestamp: Long
)

@Composable
fun Example3_MultipleMilestones() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // List of milestones with Unix timestamps
    var milestones by remember {
        mutableStateOf(listOf(
            MilestoneData(
                id = 1,
                goalEth = "10",
                deadlineUnixTimestamp = DatePickerUtils.addDays(
                    DatePickerUtils.getCurrentUnixTimestamp(),
                    30
                )
            ),
            MilestoneData(
                id = 2,
                goalEth = "20",
                deadlineUnixTimestamp = DatePickerUtils.addDays(
                    DatePickerUtils.getCurrentUnixTimestamp(),
                    60
                )
            )
        ))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Project Milestones", style = MaterialTheme.typography.headlineSmall)

        milestones.forEach { milestone ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Milestone ${milestone.id}")
                    Text("Goal: ${milestone.goalEth} ETH")

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Deadline: ")
                        Text(DatePickerUtils.formatUnixTimestamp(milestone.deadlineUnixTimestamp))

                        Button(onClick = {
                            DatePickerUtils.showDatePicker(
                                context = context,
                                initialUnixTimestamp = milestone.deadlineUnixTimestamp,
                                minDate = DatePickerUtils.getCurrentUnixTimestamp(),
                                onDateSelected = { newTimestamp ->
                                    milestones = milestones.map { m ->
                                        if (m.id == milestone.id) {
                                            m.copy(deadlineUnixTimestamp = newTimestamp)
                                        } else m
                                    }
                                }
                            )
                        }) {
                            Text("Change")
                        }
                    }

                    Text("Unix: ${milestone.deadlineUnixTimestamp}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // When submitting to backend
        Button(
            onClick = {
                // Convert to backend format
                val backendData = milestones.map {
                    mapOf(
                        "goalEth" to it.goalEth,
                        "deadline" to it.deadlineUnixTimestamp // Unix timestamp in seconds
                    )
                }
                // Send backendData to your API
                println("Sending to backend: $backendData")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit to Backend")
        }
    }
}

// ============================================================================
// Example 4: Date Validation
// ============================================================================

@Composable
fun Example4_DateValidation() {
    val context = androidx.compose.ui.platform.LocalContext.current

    var startDate by remember { mutableStateOf(DatePickerUtils.getCurrentUnixTimestamp()) }
    var endDate by remember { mutableStateOf(DatePickerUtils.addDays(DatePickerUtils.getCurrentUnixTimestamp(), 7)) }

    val isValid = startDate < endDate
    val isPast = DatePickerUtils.isPastDate(startDate)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Event Date Range", style = MaterialTheme.typography.headlineSmall)

        // Start Date
        OutlinedTextField(
            value = DatePickerUtils.formatUnixTimestamp(startDate),
            onValueChange = {},
            label = { Text("Start Date") },
            readOnly = true,
            isError = isPast || !isValid,
            supportingText = {
                if (isPast) Text("Start date is in the past", color = MaterialTheme.colorScheme.error)
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerUtils.showDatePicker(
                        context = context,
                        initialUnixTimestamp = startDate,
                        minDate = DatePickerUtils.getCurrentUnixTimestamp(),
                        maxDate = endDate,
                        onDateSelected = { startDate = it }
                    )
                }) {
                    Text("📅")
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        // End Date
        OutlinedTextField(
            value = DatePickerUtils.formatUnixTimestamp(endDate),
            onValueChange = {},
            label = { Text("End Date") },
            readOnly = true,
            isError = !isValid,
            supportingText = {
                if (!isValid) Text("End date must be after start date", color = MaterialTheme.colorScheme.error)
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerUtils.showDatePicker(
                        context = context,
                        initialUnixTimestamp = endDate,
                        minDate = startDate,
                        onDateSelected = { endDate = it }
                    )
                }) {
                    Text("📅")
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // Send to backend
                val data = mapOf(
                    "startDate" to startDate,  // Unix timestamp in seconds
                    "endDate" to endDate       // Unix timestamp in seconds
                )
                println("Sending to backend: $data")
            },
            enabled = isValid && !isPast,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

// ============================================================================
// Example 5: Converting Various Date Formats to Unix Timestamp
// ============================================================================

object DateConversionExamples {

    // Example: Current time to Unix timestamp
    fun getCurrentTime(): Long {
        return System.currentTimeMillis() / 1000L
    }

    // Example: Specific date to Unix timestamp
    fun specificDateToUnix(year: Int, month: Int, day: Int): Long {
        return DatePickerUtils.createUnixTimestamp(year, month, day)
    }

    // Example: Add days to current date
    fun getDateInFuture(daysAhead: Int): Long {
        val now = getCurrentTime()
        return DatePickerUtils.addDays(now, daysAhead)
    }

    // Example: Format Unix timestamp for display
    fun formatForDisplay(unixTimestamp: Long): String {
        return DatePickerUtils.formatUnixTimestamp(unixTimestamp)
    }

    // Example: Check if date is valid for submission
    fun isValidDeadline(unixTimestamp: Long, minDaysInFuture: Int = 1): Boolean {
        val minDate = getDateInFuture(minDaysInFuture)
        return unixTimestamp >= minDate
    }
}

// ============================================================================
// Example 6: Backend Integration
// ============================================================================

/**
 * Example of preparing data for backend submission
 */
data class ProjectSubmission(
    val name: String,
    val description: String,
    val milestones: List<MilestoneSubmission>
)

data class MilestoneSubmission(
    val goalAmountWei: String,  // Convert ETH to Wei in ViewModel
    val deadlineUnixTimestamp: Long  // Unix timestamp in seconds
)

fun createProjectSubmission(
    name: String,
    description: String,
    milestoneGoals: List<String>,  // ETH amounts
    milestoneDeadlines: List<Long>  // Unix timestamps in seconds
): ProjectSubmission {
    val milestones = milestoneGoals.zip(milestoneDeadlines).map { (goal, deadline) ->
        MilestoneSubmission(
            goalAmountWei = goal,  // Convert in ViewModel: repository.ethToWei(BigDecimal(goal))
            deadlineUnixTimestamp = deadline
        )
    }

    return ProjectSubmission(
        name = name,
        description = description,
        milestones = milestones
    )
}

// ============================================================================
// Example 7: Testing Unix Timestamp Conversion
// ============================================================================

fun testUnixTimestampConversion() {
    println("=== Unix Timestamp Conversion Tests ===")

    // Test 1: Current time
    val now = DatePickerUtils.getCurrentUnixTimestamp()
    println("Current Unix Timestamp: $now")
    println("Formatted: ${DatePickerUtils.formatUnixTimestamp(now)}")

    // Test 2: Specific date (January 1, 2024)
    val specificDate = DatePickerUtils.createUnixTimestamp(2024, 0, 1)
    println("\nJanuary 1, 2024:")
    println("Unix Timestamp: $specificDate")
    println("Formatted: ${DatePickerUtils.formatUnixTimestamp(specificDate)}")

    // Test 3: Add 30 days
    val futureDate = DatePickerUtils.addDays(now, 30)
    println("\n30 days from now:")
    println("Unix Timestamp: $futureDate")
    println("Formatted: ${DatePickerUtils.formatUnixTimestamp(futureDate)}")

    // Test 4: Validation
    println("\nValidation:")
    println("Is past date: ${DatePickerUtils.isPastDate(specificDate)}")
    println("Is today: ${DatePickerUtils.isToday(now)}")

    // Test 5: Conversion
    val millis = DatePickerUtils.unixTimestampToMillis(now)
    val backToUnix = DatePickerUtils.millisToUnixTimestamp(millis)
    println("\nRound trip conversion:")
    println("Original: $now")
    println("To millis: $millis")
    println("Back to unix: $backToUnix")
    println("Match: ${now == backToUnix}")
}

