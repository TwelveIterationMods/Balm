# Balm

Abstraction Layer (but not really)™ for Blay's multiplatform mods.

I do not recommend other modders to build on this as I will ruthlessly break backwards-compatibility as needed for my
mods.

See [the license](LICENSE) for modpack permissions etc.

#### Forge

[![Versions](http://cf.way2muchnoise.eu/versions/531761_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/balm)
[![Downloads](http://cf.way2muchnoise.eu/full_531761_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/balm)

#### Fabric

[![Versions](http://cf.way2muchnoise.eu/versions/500525_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/balm-fabric)
[![Downloads](http://cf.way2muchnoise.eu/full_500525_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/balm-fabric)

## Adding Balm to a development environment

### Using CurseMaven

Add the following to your `build.gradle`:

```groovy
repositories {
    maven { url "https://www.cursemaven.com" }
}

dependencies {
    // Replace ${balm_file_id} with the id of the file you want to depend on.
    // You can find it in the URL of the file on CurseForge (e.g. 3914527).
    // Forge: implementation fg.deobf("curse.maven:balm-531761:${balm_file_id}")
    // Fabric: modImplementation "curse.maven:balm-fabric-500525:${balm_file_id}"
}
```

### Using Nexus

Add the following to your `build.gradle`:

```groovy
repositories {
    maven { url "https://nexus.blay09.net/repository/maven-public/" }
}

dependencies {
    // Replace ${balm_version} with the version you want to depend on. 
    // You may also have to change the Minecraft version in the artifact name.
    // You can find the latest version for a given Minecraft version at https://maven.twelveiterations.com/service/rest/repository/browse/maven-public/net/blay09/mods/
    // Forge: implementation fg.deobf("net.blay09.mods:balm-forge-1.19.2:${balm_version}")
    // Fabric: modImplementation "net.blay09.mods:balm-fabric-1.19.2:${balm_version}"
}
```
