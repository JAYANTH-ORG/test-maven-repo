#!/bin/bash

echo "🧹 Cleaning up and preparing test repository..."

# Remove target directory if it exists
rm -rf target/

# Add all files
git add .

# Check if there are changes to commit
if git diff --staged --quiet; then
    echo "No changes to commit"
else
    # Commit the changes
    git commit -m "Update: Replace real-looking secrets with clearly fake ones for testing

    - Changed Stripe keys to use FAKE prefix
    - Updated Twilio SID to use FAKE prefix  
    - Maintained secret patterns for security scanning
    - Added .gitignore to exclude build artifacts

    This resolves GitHub push protection while maintaining
    security scanning test capabilities."
    
    echo "✅ Changes committed successfully"
fi

echo "🚀 Ready to push to GitHub"
echo "Run: git push origin master"
