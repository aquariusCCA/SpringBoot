---
up:
  - "[[SpringBoot 整合 Kafka]]"
---
# 第一步：安裝 Homebrew (如果尚未安裝)**

Homebrew 是 macOS 上最常用的套件管理器。如果你還沒安裝，請在「**終端機 (Terminal)**」中執行以下指令：

```shell
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

# **第二步：安裝 Kafka**

使用 Homebrew 安裝 Kafka 相當簡單，執行以下指令即可：

```shell
brew install kafka
```

---

### **重要觀念：Zookeeper 與 Kafka 的關係**

在過去，**Zookeeper 是 Kafka 運作的必要元件**。因此，當你使用 Homebrew 安裝 Kafka 時，它會自動將 Zookeeper 一併安裝。

然而，從 **Kafka 3.0 版本**開始，Kafka 引入了 **Kraft (Kafka Raft)** 模式。這個新模式讓 Kafka 能夠自己管理元數據 (metadata)，**不再需要依賴 Zookeeper**。

這就是為什麼你執行 `brew services start zookeeper` 會出現「`Error: Formula zookeeper is not installed.`」的錯誤。因為 Homebrew 已經不會再自動安裝 Zookeeper 了。

---

# **第三步：啟動 Kafka**

由於我們使用較新的 Kafka 版本，可以直接啟動 Kafka 服務，它會自動以 Kraft 模式運行。

```shell
brew services start kafka
```

**檢查服務狀態**

你可以使用以下指令確認 Kafka 是否已經成功啟動：

```shell
brew services list
```

如果 `kafka` 的狀態顯示為 `started`，就表示一切正常。

---

# **第四步：測試 Kafka**

為了確認 Kafka 是否正常運作，我們可以建立一個主題 (Topic) 並進行簡單的傳送與接收。

**1. 建立一個名為 `test-topic` 的主題**

```shell
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test-topic
```

**2. 啟動一個消費者 (Consumer)**

開啟**一個新的終端機視窗**，讓它等待接收訊息：

```shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

**3. 啟動一個生產者 (Producer)**

回到**原來的終端機視窗**，執行以下指令，然後輸入你想傳送的訊息，按 Enter 即可：

```shell
kafka-console-producer --bootstrap-server localhost:9092 --topic test-topic
```

如果你在消費者視窗中看到了你輸入的訊息，就代表 Kafka 環境已經成功設定並可以正常使用。

---

# **選用：手動安裝 Zookeeper**

如果你因為相容性等原因，還是需要使用 Zookeeper，你也可以手動安裝它。

```shell
brew install zookeeper
brew services start zookeeper
brew services start kafka
```
