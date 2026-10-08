param(
    [string]$BotaniaProjectDirectory = '',
    [switch]$Offline,
    [switch]$SkipBuild
)

$ErrorActionPreference = 'Stop'
$extraProject = Split-Path -Parent $PSScriptRoot
$extraProjectItem = Get-Item -LiteralPath $extraProject
if ($extraProjectItem.LinkType -eq 'Junction') { $extraProject = $extraProjectItem.Target[0] }
if (!$BotaniaProjectDirectory) {
    $botaniaProjects = Join-Path $extraProject '../../Botania'
    $workingCopies = @(Get-ChildItem -LiteralPath $botaniaProjects -Directory -Filter '1.21.1_NeoForge_457_*ExtraBotany')
    if ($workingCopies.Count -ne 1) { throw 'Specify BotaniaProjectDirectory to select the Botania 457 working copy.' }
    $BotaniaProjectDirectory = $workingCopies[0].FullName
}
$botaniaProject = (Resolve-Path -LiteralPath $BotaniaProjectDirectory).Path
$properties = Get-Content -Raw -LiteralPath (Join-Path $botaniaProject 'gradle.properties')
if ($properties -notmatch '(?m)^build_number=457(?:\.\d+)?\s*$' -or $properties -notmatch '(?m)^minecraft_version=1\.21\.1\s*$') {
    throw 'Expected the supplied Botania 457 project for Minecraft 1.21.1.'
}
if (!$env:JAVA_HOME -and (Test-Path -LiteralPath 'C:/Program Files/Java/jdk-21.0.12.1')) {
    $env:JAVA_HOME = 'C:/Program Files/Java/jdk-21.0.12.1'
}
$cache = Join-Path $extraProject '.codex-backup/maven'
if (!$SkipBuild) {
    $buildArguments = @('-p', $botaniaProject, ':NeoForge:assemble', "-PportCacheDirectory=$cache")
    if ($Offline) { $buildArguments += '--offline' }
    $previousErrorPreference = $ErrorActionPreference
    try {
        $ErrorActionPreference = 'Continue'
        & (Join-Path $botaniaProject 'gradlew.bat') @buildArguments
        $buildExitCode = $LASTEXITCODE
    } finally {
        $ErrorActionPreference = $previousErrorPreference
    }
    if ($buildExitCode -ne 0) { throw "Botania build failed (exit $buildExitCode)." }
}
$artifact = 'botania-neoforge-1.21.1'
$version = '457.1-SNAPSHOT'
$jar = Join-Path $botaniaProject "NeoForge/build/libs/$artifact-$version.jar"
if (!(Test-Path -LiteralPath $jar)) { throw "Botania output is missing: $jar" }
$destination = Join-Path $cache "vazkii/botania/$artifact/$version"
New-Item -ItemType Directory -Path $destination -Force | Out-Null
Copy-Item -LiteralPath $jar -Destination (Join-Path $destination "$artifact-$version.jar")
$sources = Join-Path $botaniaProject "NeoForge/build/libs/$artifact-$version-sources.jar"
if (Test-Path -LiteralPath $sources) {
    Copy-Item -LiteralPath $sources -Destination (Join-Path $destination "$artifact-$version-sources.jar")
}
# ExtraBotany declares Patchouli and Curios directly. This private artifact is not published upstream.
$pom = @"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>vazkii.botania</groupId>
  <artifactId>$artifact</artifactId>
  <version>$version</version>
  <description>Local build of the supplied Botania 457 NeoForge port.</description>
</project>
"@
Set-Content -LiteralPath (Join-Path $destination "$artifact-$version.pom") -Value $pom -Encoding UTF8
Write-Output "Cached local Botania: $destination"
