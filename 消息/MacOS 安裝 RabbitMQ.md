---
up:
  - "[[SpringBoot 整合 RabbitMQ]]"
---
以下是在 **MacBook Pro（Apple M1 / Apple Silicon）** 上安裝 **Erlang** 與 **RabbitMQ** 的「一步一步」做法。我會先處理最容易踩的相容性地雷，再給你可複製貼上的命令。

> 關鍵提醒（別跳過）：RabbitMQ 目前**不支援 Erlang/OTP 28**；請鎖定 **Erlang 27.x（或 26.x）**，否則會無法啟動或行為異常。([rabbitmq.com][1])

---

# 0) 檢查 Homebrew 與環境

1. 如果你尚未安裝 Homebrew（Apple Silicon 會安裝在 `/opt/homebrew`）：

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Homebrew 在 Apple Silicon 的預設安裝前綴是 **/opt/homebrew**；安裝後請把 `bin` 與 `sbin` 加入 PATH（zsh）：

```bash
echo 'export PATH="/opt/homebrew/bin:/opt/homebrew/sbin:$PATH"' >> ~/.zshrc
exec zsh
```

---

# 1) 安裝對應版本的 Erlang（27.x）

> 由於 Erlang 28 暫不支援，請安裝 **`erlang@27`**，並「固定版本」避免升級到 28。

```bash
brew update
brew install erlang@27
# 將 erlang@27 設為預設鏈結（若系統同時裝了 erlang 28，先 unlink）
brew unlink erlang || true
brew link --overwrite --force erlang@27

# 避免之後 brew upgrade 把它升上 28
brew pin erlang@27
```

（`erlang@27` 為 Homebrew 的版本化套件；`brew pin` 用來阻止升級該套件。）

---

# 2) 安裝 RabbitMQ

```bash
brew install rabbitmq
# 建議把 /opt/homebrew/sbin 放進 PATH 以便直接呼叫 CLI（若前面已加入可略過）
echo 'export PATH="/opt/homebrew/sbin:$PATH"' >> ~/.zshrc
exec zsh
```

Homebrew 版的安裝位置與 CLI 路徑說明、以及 Apple Silicon 下的 `sbin` 路徑見官方指南。 

> 注意：Homebrew 的 `rabbitmq` formula 目前顯示相依 `erlang 28`；因此**務必**讓 PATH 指向你剛剛連結的 `erlang@27`，並保持 `erlang@27` 被 pin，否則未來升級可能被換成 28。

---

# 3) 啟動服務

背景常駐（登入後自動啟）：

```bash
brew services start rabbitmq
```

---

# 4) 啟用 Management UI（Web 介面）

```bash
rabbitmq-plugins enable rabbitmq_management
```

打開瀏覽器到 `http://localhost:15672/`。
Management UI 使用的埠是 **15672**（啟用 management 外掛後才會開）。

**預設帳號密碼**：`guest` / `guest`，且**只允許 localhost 登入**（安全性考量）。

# 5) 驗證版本與健康狀態（確保跑在 Erlang 27）

```bash
# RabbitMQ 是否在跑
rabbitmq-diagnostics is_running

# 顯示 RabbitMQ 版本
rabbitmq-diagnostics server_version

# 顯示節點所用的 Erlang/OTP 版本（應為 27.x）
rabbitmq-diagnostics erlang_version

# 取得更完整狀態（含 RabbitMQ 與 Erlang 版本）
rabbitmqctl status
```
---


# 6) 解除安裝／重裝（清乾淨）

```bash
brew services stop rabbitmq
brew uninstall rabbitmq
# 刪除 Homebrew 版 RabbitMQ 的設定與資料（Apple Silicon 安裝路徑）
rm -rf /opt/homebrew/etc/rabbitmq/ /opt/homebrew/opt/rabbitmq/ /opt/homebrew/var/lib/rabbitmq/
# 需要時再：brew uninstall erlang@27 && brew unpin erlang@27
```

---