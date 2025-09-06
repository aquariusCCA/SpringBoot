---
up:
  - "[[SpringBoot 整合 ActiveMQ]]"
---
開門見山：**先決定你要裝的是 ActiveMQ Classic（5.x/6.x）還是 ActiveMQ Artemis（2.x）**。

* **新專案/需要 JMS 2.0、AMQP 等：選 Artemis**。
* **維護舊系統/與 5.x 相容：選 Classic**。

以下給你在 **MacBook Pro (Apple M1)** 上的最穩方案（皆原生 arm64，不需 Rosetta）：

---

# 方案 A：用 Homebrew 安裝 **ActiveMQ Classic**（最省事）

> 目前 Homebrew `activemq` 安裝的是 Classic **6.1.x**，支援 Apple Silicon。

1. 安裝（若你未裝 brew，請先到 brew\.sh 安裝；Apple Silicon 的 Homebrew 前綴在 `/opt/homebrew`）：

```bash
brew install activemq
```

（Homebrew 頁面也說明了 arm64 瓶裝支援）

2. 啟動服務（背景常駐）：

```bash
brew services start activemq
# 檢視狀態
brew services list
# 停止
brew services stop activemq
```

3. 驗證 Web Console（預設埠 **8161**）：

* 開啟：[http://localhost:8161/admin](http://localhost:8161/admin)
* **預設帳密：`admin` / `admin`**（建議立即更改）
  以上為官方文件寫明。

4. （可選）修改預設密碼與埠號：

* 密碼：編輯 `conf/jetty-realm.properties`。
* Web Console 埠或 OpenWire 61616 埠：到 `conf/jetty.xml` / `conf/activemq.xml` 調整。

> **Java 相容性（Classic）**：
>
> * 6.1.x 需 **Java 17+**；5.19.x 需 **Java 11+**。建議直接用 Java 17 LTS。

（若你需要指定 JDK 版本）

```bash
brew install openjdk@17
#（必要時建立系統可見的 JDK symlink）
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk \
  /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

---