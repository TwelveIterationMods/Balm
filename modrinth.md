<p>
    <a style="text-decoration: none;" href="https://modrinth.com/mod/balm"> 
        <img src="https://blay09.net/files/brand/requires_balm.png" alt="Requires Balm" width="217" height="51" /> 
    </a>
    <img src="https://blay09.net/files/brand/spacer.png" alt="" width="20" height="51" />
    <a style="text-decoration: none;" href="https://www.patreon.com/blay09"> 
        <img src="https://blay09.net/files/brand/patreon.png" alt="Become a Patron" width="217" height="51" /> 
    </a> 
    <img src="https://blay09.net/files/brand/spacer.png" alt="" width="21" height="51" /> 
    <a style="text-decoration: none;" href="https://twitter.com/BlayTheNinth">
        <img src="https://blay09.net/files/brand/twitter.png" alt="Follow me on Twitter" width="51" height="51" />
    </a>
    <a style="text-decoration: none;" href="https://discord.gg/VAfZ2Nau6j">
        <img src="https://blay09.net/files/brand/discord.png" alt="Join our Discord" width="51" height="51" />
    </a>
</p>

## What is this?

Abstraction Layer (but not really)™ for Blay's multiplatform mods. This is a library mod, it does not add anything on its own.

I do not recommend other modders to build on this as I will ruthlessly break backwards-compatibility as needed for my mods.

Note that this is not a magic solution for running Forge and Fabric mods together, it's only a library my mods will depend on to make publishing for both versions easier.

## Why is this?

I can't just switch to Fabric and abandon all existing Forge users, but I also don't want to miss a Fabric train if there is one.

Trying a port for fun showed that most of the platform-specific things can easily be hidden away from the actual mod code which doesn't have to care about modloader backends, which should hopefully make it much easier to support both mods at the same time.

There's other libraries that do the same thing, but given the amount of mods I maintain I don't want to put all my money on one boat only to deal with headaches once those other mods stop being supported.

## How does this?

It just wraps all platform-specific code behind a unified API (in the simplest way possible, there is no magic involved) and provides superclasses to use in cases where platform-specific methods are added to Vanilla classes.

It also adds a simple network system (similar to Forge's) and a config layer with sync support. For Fabric it depends on Cloth Config, for Forge it will use the default Forge config system in the backend.

## Who is this?

Hi, I'm Blay and my Twitter is at [@BlayTheNinth](https://twitter.com/BlayTheNinth).
