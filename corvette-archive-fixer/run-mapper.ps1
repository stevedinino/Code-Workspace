# run-mapper.ps1
# Hard-coded paths version

Write-Host "Creating virtual environment..."
if (-Not (Test-Path ".venv")) {
    python -m venv .venv
}

Write-Host "Activating virtual environment..."
& .\.venv\Scripts\Activate.ps1

# Hard-coded archive root and output file
$ArchiveRoot = "D:\repairs.willcoxcorvette.com"
$OutputFile  = "C:\Code Workspace\corvette-archive-fixer\maps\fs-map.json"

Write-Host "Running mapper..."
python mapper\main.py $ArchiveRoot -o $OutputFile

Write-Host "Done. JSON written to $OutputFile"