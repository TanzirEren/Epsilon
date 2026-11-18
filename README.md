# Epsilon — Epsilon Fox ModS app

- Package: `com.epsilon.fix.modz`
- Telegram: https://t.me/EpsilonFoxModS
- Java + XML, no external libraries, light glass UI

## Build with GitHub Actions
1. Create a new GitHub repo and upload everything in this folder (keep `.github/workflows/build.yml`).
2. Open **Actions → Build Epsilon APK → Run workflow** (it also runs on every push).
3. When it finishes, download the **Epsilon-APK** artifact → `Epsilon-v1.0.0.apk`.

The APK is signed with `app/epsilon.keystore` (password `epsilon123`, alias `epsilon`) — keep it so updates install over older versions.
