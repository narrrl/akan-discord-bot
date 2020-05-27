### Configure config.json

- add your bot token to the config.json, to get a bot token visit:
https://discordapp.com/developers/applications
- successReaction true if you want reaction upon command execution
- prefix is the bot prefix

Your config should look like this:
```json
{
    "token": "2D34Nad23zMwN2323zdas45y.Xsmsig.jo_dai2fwsx0Taadd2naz0_Nvw",
    "prefix": "a!",
    "successReaction": "false"
}
```
p.s. token is not real.

### Execute the Jar

run jar with:

    java -jar akan-discord-bot-1.1-all.jar

**Use JAVA 14!**

You can check your java version with:

    java -version
    
output should be something like that:

    openjdk version "14.0.1" 2020-04-14
    OpenJDK Runtime Environment (build 14.0.1+7)
    OpenJDK 64-Bit Server VM (build 14.0.1+7, mixed mode, sharing)


If you did everything right, bot should be up.
