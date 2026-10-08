param([switch]$IncludeSources)

$ErrorActionPreference = 'Stop'
$repositoryRoot = Split-Path -Parent $PSScriptRoot
$properties = @{}
Get-Content -LiteralPath (Join-Path $repositoryRoot 'gradle.properties') | ForEach-Object {
    if ($_ -match '^([^#=]+)=(.*)$') { $properties[$matches[1].Trim()] = $matches[2].Trim() }
}
$cacheRoot = Join-Path $repositoryRoot '.codex-backup/maven'
$minecraft = $properties['minecraft_version']
$jared = 'https://maven.blamejared.com'
$latvian = 'https://maven.latvian.dev/releases'
$artifacts = @(
    @('vazkii.patchouli', 'Patchouli-xplat', $properties['patchouli_version'], '', $jared),
    @('vazkii.patchouli', 'Patchouli', "$($properties['patchouli_version'])-NEOFORGE", '', $jared),
    @('mezz.jei', "jei-$minecraft-common-api", $properties['jei_version'], '', $jared),
    @('mezz.jei', "jei-$minecraft-neoforge", $properties['jei_version'], '', $jared),
    @('top.theillusivec4.curios', 'curios-neoforge', $properties['curios_version'], 'api', 'https://maven.theillusivec4.top'),
    @('top.theillusivec4.curios', 'curios-neoforge', $properties['curios_version'], '', 'https://maven.theillusivec4.top'),
    @('dev.emi', 'emi-neoforge', $properties['emi_version'], 'api', 'https://maven.terraformersmc.com'),
    @('dev.emi', 'emi-xplat-mojmap', $properties['emi_version'], 'api', 'https://maven.terraformersmc.com'),
    @('dev.emi', 'emi-neoforge', $properties['emi_version'], '', 'https://maven.terraformersmc.com'),
    @('com.unascribed', 'ears-api', '1.4.5', '', 'https://repo.unascribed.com'),
    @('dev.latvian.mods', 'kubejs-neoforge', $properties['kubejs_version'], '', $latvian),
    @('dev.latvian.mods', 'rhino', $properties['rhino_version'], '', $latvian),
    @('dev.architectury', 'architectury-neoforge', $properties['architectury_version'], '', 'https://maven.architectury.dev')
)

function Save-ArtifactFile([string]$Url, [string]$Destination) {
    if (Test-Path -LiteralPath $Destination) { return }
    $temporary = "$Destination.download"
    try {
        Invoke-WebRequest -UseBasicParsing -Uri $Url -OutFile $temporary -TimeoutSec 45
        Move-Item -LiteralPath $temporary -Destination $Destination
    } finally {
        if (Test-Path -LiteralPath $temporary) { Remove-Item -LiteralPath $temporary }
    }
}

foreach ($entry in $artifacts) {
    $group, $artifact, $version, $classifier, $maven = $entry
    $directoryVersion = $version
    if ($version -match '^(.+)-\d{8}\.\d{6}-\d+$') { $directoryVersion = "$($matches[1])-SNAPSHOT" }
    $relative = "$($group.Replace('.', '/'))/$artifact/$directoryVersion"
    $destination = Join-Path $cacheRoot $relative
    New-Item -ItemType Directory -Path $destination -Force | Out-Null
    $baseName = "$artifact-$version"
    Save-ArtifactFile "$maven/$relative/$baseName.pom" (Join-Path $destination "$baseName.pom")
    $jarName = if ($classifier) { "$baseName-$classifier.jar" } else { "$baseName.jar" }
    Save-ArtifactFile "$maven/$relative/$jarName" (Join-Path $destination $jarName)
    if ($directoryVersion -ne $version) {
        Save-ArtifactFile "$maven/$relative/maven-metadata.xml" (Join-Path $destination 'maven-metadata.xml')
    }
    if ($IncludeSources -and !$classifier) {
        Save-ArtifactFile "$maven/$relative/$baseName-sources.jar" (Join-Path $destination "$baseName-sources.jar")
    }
    Write-Output "Cached ${group}:${artifact}:${version}${classifier}"
}
$neoVersion = $properties['neo_forge_version']
$neoRelative = "net/neoforged/neoforge/$neoVersion"
$neoDestination = Join-Path $cacheRoot $neoRelative
New-Item -ItemType Directory -Path $neoDestination -Force | Out-Null
foreach ($suffix in @('.pom', '.module', '-userdev.jar', '-universal.jar', '-sources.jar', '-moddev-config.json')) {
    $name = "neoforge-$neoVersion$suffix"
    Save-ArtifactFile "https://maven.neoforged.net/releases/$neoRelative/$name" (Join-Path $neoDestination $name)
}
Write-Output "Cached NeoForge $neoVersion"
Write-Output "Dependency mirror: $cacheRoot"
