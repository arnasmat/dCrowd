package com.arnasmat.dcrowd.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arnasmat.dcrowd.util.DatePickerUtils

/**
 * Example of an improved MilestoneCard component using DatePickerUtils
 *
 * This demonstrates best practices for:
 * - Using reusable utility functions
 * - Proper Unix timestamp handling
 * - Clean UI/UX patterns
 */
@Composable
fun ImprovedMilestoneCard(
    goalAmountEth: String,
    deadlineUnixTimestamp: Long,
    onGoalChange: (String) -> Unit,
    onDeadlineChange: (Long) -> Unit,
    onDelete: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Format deadline for display using utility
    val dateText = remember(deadlineUnixTimestamp) {
        DatePickerUtils.formatUnixTimestamp(deadlineUnixTimestamp)
    }

    // Determine if date is in the past for validation
    val isPastDate = remember(deadlineUnixTimestamp) {
        DatePickerUtils.isPastDate(deadlineUnixTimestamp)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = goalAmountEth,
                    onValueChange = onGoalChange,
                    label = { Text("Goal (ETH)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = {
                        if (goalAmountEth.toDoubleOrNull()?.let { it <= 0 } == true) {
                            Text("Goal must be greater than 0", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                // Date picker field - read-only with click action
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { /* read-only */ },
                    label = { Text("Deadline") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            DatePickerUtils.showDatePicker(
                                context = context,
                                initialUnixTimestamp = deadlineUnixTimestamp,
                                minDate = DatePickerUtils.getCurrentUnixTimestamp(),
                                onDateSelected = onDeadlineChange
                            )
                        },
                    singleLine = true,
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select date"
                        )
                    },
                    supportingText = {
                        if (isPastDate) {
                            Text("Date is in the past", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    isError = isPastDate
                )
            }

            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete milestone")
                }
            }
        }
    }
}

/**
 * Example composable showing how to use the date picker for a single date input
 */
@Composable
fun DateInputField(
    label: String,
    unixTimestamp: Long,
    onDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    minDate: Long? = null,
    maxDate: Long? = null,
    isRequired: Boolean = false,
    errorMessage: String? = null
) {
    val context = LocalContext.current

    val dateText = remember(unixTimestamp) {
        DatePickerUtils.formatUnixTimestamp(unixTimestamp)
    }

    OutlinedTextField(
        value = dateText,
        onValueChange = { /* read-only */ },
        label = { Text(if (isRequired) "$label *" else label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                DatePickerUtils.showDatePicker(
                    context = context,
                    initialUnixTimestamp = unixTimestamp,
                    minDate = minDate,
                    maxDate = maxDate,
                    onDateSelected = onDateChange
                )
            },
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select date"
            )
        },
        supportingText = errorMessage?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        isError = errorMessage != null
    )
}

/**
 * Example showing date range picker
 */
@Composable
fun DateRangeInput(
    startDate: Long,
    endDate: Long,
    onStartDateChange: (Long) -> Unit,
    onEndDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DateInputField(
            label = "Start Date",
            unixTimestamp = startDate,
            onDateChange = onStartDateChange,
            maxDate = endDate, // Start date cannot be after end date
            errorMessage = if (startDate > endDate) "Start date must be before end date" else null
        )

        DateInputField(
            label = "End Date",
            unixTimestamp = endDate,
            onDateChange = onEndDateChange,
            minDate = startDate, // End date cannot be before start date
            errorMessage = if (endDate < startDate) "End date must be after start date" else null
        )
    }
}

