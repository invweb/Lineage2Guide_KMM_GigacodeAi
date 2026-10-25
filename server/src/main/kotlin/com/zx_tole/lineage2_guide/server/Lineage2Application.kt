package com.zx_tole.lineage2_guide.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.BindException
import java.net.ServerSocket

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    
    // Check if port is already in use
    if (isPortInUse(port)) {
        println("⚠️  WARNING: Port $port is already in use!")
        println()
        println("The server cannot start because port $port is occupied.")
        println()
        
        // Try to get user input (non-blocking with timeout)
        val shouldFreePort = promptUser("Do you want to free the port? (y/n): ")
        
        if (shouldFreePort) {
            println("🔄 Freeing port $port...")
            val freed = freePort(port)
            if (freed) {
                println("✅ Port $port has been freed successfully.")
            } else {
                println("❌ Failed to free port $port. Please close the application using this port manually.")
                return
            }
        } else {
            println("ℹ️  Server startup cancelled by user.")
            return
        }
    }
    
    try {
        embeddedServer(Netty, port = port) {
            module()
        }.start(wait = true)
    } catch (e: BindException) {
        println("❌ ERROR: Port $port is still in use after attempt to free it.")
        println("Please close the application using port $port and try again.")
        println()
        println("Tip: You can find the process using this port with:")
        println("  lsof -i :$port")
        println()
        println("Then kill it with:")
        println("  kill -9 <PID>")
    }
}

/**
 * Check if a port is already in use
 */
private fun isPortInUse(port: Int): Boolean {
    return try {
        ServerSocket(port).use { return false }
        false
    } catch (e: Exception) {
        true
    }
}

/**
 * Free a port by killing the process using it (Unix/macOS only)
 */
private fun freePort(port: Int): Boolean {
    return try {
        // Get the PID using the port
        val getPidProcess = Runtime.getRuntime().exec(arrayOf("lsof", "-ti", ":$port"))
        val pidReader = BufferedReader(InputStreamReader(getPidProcess.inputStream))
        val pids = mutableListOf<String>()
        var line: String?
        while (pidReader.readLine().also { line = it } != null) {
            line?.let { pids.add(it) }
        }
        getPidProcess.waitFor()
        
        if (pids.isEmpty()) {
            println("⚠️  No process found using port $port")
            return false
        }
        
        println("Found processes using port $port: ${pids.joinToString(", ")}")
        
        // Kill each process
        var success = true
        for (pid in pids) {
            try {
                println("Killing process $pid...")
                val killProcess = Runtime.getRuntime().exec(arrayOf("kill", "-9", pid))
                killProcess.waitFor()
            } catch (e: Exception) {
                println("❌ Failed to kill process $pid: ${e.message}")
                success = false
            }
        }
        
        // Wait a bit for port to be freed
        Thread.sleep(1000)
        
        // Verify port is freed
        success && !isPortInUse(port)
    } catch (e: Exception) {
        println("❌ Error freeing port: ${e.message}")
        false
    }
}

/**
 * Prompt user for input with a timeout
 */
private fun promptUser(question: String): Boolean {
    return try {
        print("$question ")
        System.out.flush()
        
        val reader = BufferedReader(InputStreamReader(System.`in`))
        val input = reader.readLine()?.trim()?.lowercase()
        
        input == "y" || input == "yes" || input == "да" || input == "y"
    } catch (e: Exception) {
        println()
        println("⚠️  Could not read user input. Defaulting to NO.")
        false
    }
}
