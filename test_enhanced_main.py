#!/usr/bin/env python3
"""
Test the enhanced main.py scanner locally
"""
import os
import sys
import asyncio

# Add the action path to sys.path to import main.py
sys.path.append(r"c:\Users\2121659\Shared-libs\.github\actions\secureflow-scan")

# Set environment variables for testing
os.environ["INPUT_TARGET"] = "."
os.environ["INPUT_PROJECT_TYPE"] = "java-maven"
os.environ["INPUT_SCAN_TYPES"] = "sast,secrets,dependencies"
os.environ["INPUT_SEVERITY_THRESHOLD"] = "low"
os.environ["INPUT_OUTPUT_FORMAT"] = "sarif"
os.environ["INPUT_OUTPUT_FILE"] = "test-results"
os.environ["INPUT_GENERATE_HTML"] = "true"
os.environ["INPUT_CONFIG_FILE"] = ".secureflow.yaml"
os.environ["INPUT_FAIL_ON_FINDINGS"] = "false"

# Import and run main
try:
    from main import main
    print("=== RUNNING ENHANCED SECUREFLOW SCANNER ===")
    asyncio.run(main())
except SystemExit as e:
    print(f"Scanner completed with exit code: {e.code}")
except Exception as e:
    print(f"Scanner failed with error: {e}")
    import traceback
    traceback.print_exc()
