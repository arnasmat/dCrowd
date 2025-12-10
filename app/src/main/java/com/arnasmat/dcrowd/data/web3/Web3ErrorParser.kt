package com.arnasmat.dcrowd.data.web3

import android.util.Log
import org.json.JSONObject

// this shit is vibe coded as fuck because I just don't quite give a shit about this lmaoo
object Web3ErrorParser {

    /**
     * Parses a Web3j error message to extract the reason.
     *
     * Handles multiple error formats:
     * - JSON errors with "error.message" field
     * - "execution reverted: <reason>" format
     * - "Error processing transaction request: <reason>" format
     * - Generic fallback
     *
     * @param errorMessage The raw error message from Web3j
     * @return A cleaned, user-friendly error message
     */
    fun parseErrorMessage(errorMessage: String?): String {
        Log.d("Web3ErrorParser", "Raw error message: $errorMessage")

        if (errorMessage.isNullOrBlank()) {
            return "Unknown error occurred"
        }

        // Try to parse as JSON first
        val jsonReason = tryParseJsonError(errorMessage)
        Log.d("Web3ErrorParser", "JSON reason extracted: $jsonReason")

        if (jsonReason != null) {
            Log.d("Web3ErrorParser", "Returning JSON reason directly: $jsonReason")
            return jsonReason
        }

        // Try to extract reason from common error patterns
        val extracted = extractReasonFromMessage(errorMessage)
        Log.d("Web3ErrorParser", "Final extracted (from patterns): $extracted")
        return extracted
    }

    /**
     * Attempts to parse the error message as JSON and extract the error reason.
     */
    private fun tryParseJsonError(errorMessage: String): String? {
        return try {
            val json = JSONObject(errorMessage)
            Log.d("Web3ErrorParser", "Parsed as JSON object with keys: ${json.keys().asSequence().toList()}")

            // Check if any key looks like a transaction hash (starts with 0x)
            // This handles Ganache-style errors where the error is nested under the tx hash
            val txHashKey = json.keys().asSequence().firstOrNull { it.startsWith("0x") }
            if (txHashKey != null) {
                Log.d("Web3ErrorParser", "Found transaction hash key: $txHashKey")
                val txErrorObj = json.getJSONObject(txHashKey)

                // Try to extract reason from the nested object
                val reason = txErrorObj.optString("reason", "")
                if (reason.isNotBlank()) {
                    Log.d("Web3ErrorParser", "Found reason in tx hash object: $reason")
                    return reason
                }

                // Try error field in nested object
                val error = txErrorObj.optString("error", "")
                if (error.isNotBlank() && error != "revert") {
                    Log.d("Web3ErrorParser", "Found error in tx hash object: $error")
                    return error
                }
            }

            // Try different JSON paths where the error might be
            // Priority order: nested error.message > error.data > message > reason
            when {
                json.has("error") -> {
                    val errorField = json.get("error")
                    Log.d("Web3ErrorParser", "Found 'error' field: $errorField")

                    // error could be a string or object
                    if (errorField is String) {
                        return errorField.ifBlank { null }
                    }

                    if (errorField is JSONObject) {
                        val errorObj = errorField

                        // Try error.message
                        val message = errorObj.optString("message", "")
                        if (message.isNotBlank()) {
                            Log.d("Web3ErrorParser", "Found error.message: $message")
                            return message
                        }

                        // Try error.data (sometimes contains the revert reason)
                        val data = errorObj.optString("data", "")
                        if (data.isNotBlank()) {
                            Log.d("Web3ErrorParser", "Found error.data: $data")
                            return data
                        }

                        // Try error.reason
                        val reason = errorObj.optString("reason", "")
                        if (reason.isNotBlank()) {
                            Log.d("Web3ErrorParser", "Found error.reason: $reason")
                            return reason
                        }
                    }
                    null
                }
                json.has("message") -> {
                    val msg = json.getString("message")
                    Log.d("Web3ErrorParser", "Found 'message' field: $msg")
                    msg.ifBlank { null }
                }
                json.has("reason") -> {
                    val reason = json.getString("reason")
                    Log.d("Web3ErrorParser", "Found 'reason' field: $reason")
                    reason.ifBlank { null }
                }
                json.has("data") -> {
                    val data = json.getString("data")
                    Log.d("Web3ErrorParser", "Found 'data' field: $data")
                    data.ifBlank { null }
                }
                else -> {
                    Log.d("Web3ErrorParser", "No recognized JSON fields found")
                    null
                }
            }
        } catch (e: Exception) {
            Log.d("Web3ErrorParser", "Not a valid JSON: ${e.message}")
            // Not a valid JSON, continue with other parsing methods
            null
        }
    }

    /**
     * Extracts the meaningful reason from various error message formats.
     */
    private fun extractReasonFromMessage(message: String): String {
        Log.d("Web3ErrorParser", "extractReasonFromMessage input: $message")

        // Pattern 1: "execution reverted: <reason>"
        val revertedPattern = Regex("""execution reverted:\s*(.+?)(?:\n|$|"|})""", RegexOption.IGNORE_CASE)
        revertedPattern.find(message)?.let {
            val reason = it.groupValues[1].trim().cleanErrorMessage()
            Log.d("Web3ErrorParser", "Pattern 1 matched: $reason")
            if (reason.isNotBlank()) return reason
        }

        // Pattern 2: "Error processing transaction request: <reason>"
        val transactionErrorPattern = Regex("""Error processing transaction request:\s*(.+?)(?:\n|$|"|})""", RegexOption.IGNORE_CASE)
        transactionErrorPattern.find(message)?.let {
            val reason = it.groupValues[1].trim().cleanErrorMessage()
            if (reason.isNotBlank()) return reason
        }

        // Pattern 3: Look for "revert <reason>" (Solidity revert)
        val revertPattern = Regex("""revert\s+(.+?)(?:\n|$|"|})""", RegexOption.IGNORE_CASE)
        revertPattern.find(message)?.let {
            val reason = it.groupValues[1].trim().cleanErrorMessage()
            if (reason.isNotBlank()) return reason
        }

        // Pattern 4: Extract content after "message:" or "reason:" (handles JSON strings)
        val messagePattern = Regex(""""(?:message|reason)"\s*:\s*"([^"]+)"""", RegexOption.IGNORE_CASE)
        messagePattern.find(message)?.let {
            val reason = it.groupValues[1].trim().cleanErrorMessage()
            if (reason.isNotBlank()) return reason
        }

        // Pattern 5: Simple key:value format (non-quoted)
        val simplePattern = Regex("""(?:message|reason):\s*([^\n,}]+)""", RegexOption.IGNORE_CASE)
        simplePattern.find(message)?.let {
            val reason = it.groupValues[1].trim().cleanErrorMessage()
            if (reason.isNotBlank()) return reason
        }

        // Pattern 6: Look for any quoted strings that might be the error
        val quotedPattern = Regex(""""([^"]{10,})"""")
        val matches = quotedPattern.findAll(message).toList()
        if (matches.isNotEmpty()) {
            // Return the longest quoted string as it's likely the actual error
            val longestMatch = matches.maxByOrNull { it.groupValues[1].length }
            longestMatch?.let {
                val reason = it.groupValues[1].trim().cleanErrorMessage()
                if (reason.isNotBlank() && !reason.startsWith("{")) return reason
            }
        }

        // If no pattern matches, return the original message cleaned up
        return message.cleanErrorMessage()
    }

    /**
     * Cleans up the error message by removing common technical prefixes and suffixes.
     */
    private fun String.cleanErrorMessage(): String {
        var cleaned = this.trim()

        // Remove quotes
        if (cleaned.length > 1 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(1, cleaned.length - 1)
        }
        if (cleaned.length > 1 && cleaned.startsWith("'") && cleaned.endsWith("'")) {
            cleaned = cleaned.substring(1, cleaned.length - 1)
        }

        // Unescape common escape sequences
        cleaned = cleaned
            .replace("\\\"", "\"")
            .replace("\\'", "'")
            .replace("\\n", " ")
            .replace("\\r", "")
            .replace("\\t", " ")

        // Remove technical noise
        cleaned = cleaned
            .removePrefix("Error: ")
            .removePrefix("Exception: ")
            .trim()

        // Remove trailing punctuation if it's not meaningful
        if (cleaned.endsWith("},") || cleaned.endsWith("}")) {
            cleaned = cleaned.substringBeforeLast("}")
        }

        // Limit length to avoid extremely long errors
        if (cleaned.length > 200) {
            cleaned = cleaned.take(200) + "..."
        }

        return cleaned.trim()
    }
}

/**
 * Extension function to parse exceptions and extract meaningful error messages.
 */
fun Throwable.parseWeb3Error(): String {
    return try {
        Web3ErrorParser.parseErrorMessage(this.message)
    } catch (e: Exception) {
        // Fallback if parsing itself fails
        Log.e("Web3ErrorParser", "Exception during parsing: ${e.message}", e)
        this.message ?: "Unknown error occurred"
    }
}

