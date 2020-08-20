# Akan Discord-Bot

This Bot just is a fun project for my own Discord Server,
don't expect high quality code or unique features.
You can download and edit the bot however you want.

## Installation

Clone the repo and run `gradle build` and then go to `build/libs/`. You will find a `akan-discord-bot-{$VERSION}-all.jar`. Copy this, and the `config.json` from `src/main/resources/` into one folder (anywhere on your computer).
Now execute `java -jar akan-discord-bot-{$VERSION}-all.jar` inside the directory with the jar and the config.
Don't forget to put your bot-token into the config! And __NEVER__ share the bot-token with anyone!

The config shoulde look something like this:

```json
{
  "activity":"a!help",
  "prefix":"a!",
  "successReaction":"true",
  "activityType":"listening",
  "token":"YOUR BOT TOKEN"
}
```

The bot directory should look like this:
```
bot-directory
    ├── akan-discord-bot-1.2-all.jar
    └── config.json
```

The bot will create a log directory on the first start:
```
bot-directory
    ├── akan-discord-bot-1.2-all.jar
    ├── config.json
    └── logs
        └── YYYY-MM-DDlog.json
```

## Contact

- [Discord](https://discord.gg/FZ546P3)
- Discord-Tag: ニルス#0420
- Email: nils.pukropp@outlook.de
