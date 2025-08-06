# PowerShell script to clean up and commit changes

Write-Host "🧹 Cleaning up and preparing test repository..." -ForegroundColor Cyan

# Remove target directory if it exists
if (Test-Path "target") {
    Remove-Item -Path "target" -Recurse -Force
    Write-Host "Removed target directory" -ForegroundColor Green
}

# Add all files
git add .

# Check if there are changes to commit
$changes = git diff --staged --name-only
if ($changes) {
    # Commit the changes
    git commit -m "Update: Replace real-looking secrets with clearly fake ones for testing

- Changed Stripe keys to use FAKE prefix
- Updated Twilio SID to use FAKE prefix  
- Maintained secret patterns for security scanning
- Added .gitignore to exclude build artifacts

This resolves GitHub push protection while maintaining
security scanning test capabilities."
    
    Write-Host "✅ Changes committed successfully" -ForegroundColor Green
} else {
    Write-Host "No changes to commit" -ForegroundColor Yellow
}

Write-Host "🚀 Ready to push to GitHub" -ForegroundColor Cyan
Write-Host "Run: git push origin master" -ForegroundColor White
