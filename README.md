# AI Java Demo

基于 Spring Boot 3.3 和阿里云百炼（DashScope）的 AI 应用示例项目。

## 项目简介

这是一个展示如何使用 Spring Boot 集成阿里云百炼 AI 服务的示例项目，包含了：

- 🤖 DashScope AI 模型调用
- 💬 百炼 Agent 流式对话
- 🏗️ 标准的三层架构（Controller-Service-DTO）
- 🛡️ 统一异常处理
- 📝 Log4j2 日志配置
- ✅ 单元测试和集成测试

## 技术栈

- **框架**: Spring Boot 3.3.7
- **JDK**: Java 17
- **构建工具**: Maven
- **日志**: Log4j2
- **AI SDK**: Spring AI Alibaba DashScope Starter
- **响应式编程**: Spring WebFlux
- **工具**: Lombok, Commons Lang3
- **测试**: JUnit 5, Mockito

## 项目结构

```
ai-java-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/ai/
│   │   │   ├── AiJavaDemoApplication.java      # 应用入口
│   │   │   ├── config/                         # 配置类
│   │   │   │   ├── DashScopeProperties.java    # DashScope配置属性
│   │   │   │   └── WebClientConfig.java        # WebClient配置
│   │   │   ├── controller/                     # 控制器层
│   │   │   │   ├── AiController.java           # AI问答控制器
│   │   │   │   └── BailianAgentStreamController.java  # 百炼Agent控制器
│   │   │   ├── service/                        # 服务层
│   │   │   │   ├── AiService.java              # AI服务
│   │   │   │   └── BailianAgentService.java    # 百炼Agent服务
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   │   ├── request/                    # 请求DTO
│   │   │   │   │   └── AiQuestionRequest.java
│   │   │   │   └── response/                   # 响应DTO
│   │   │   │       ├── ApiResponse.java
│   │   │   │       └── AiResponse.java
│   │   │   └── exception/                      # 异常处理
│   │   │       ├── AiServiceException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.yml                 # 应用配置
│   │       └── log4j2-spring.xml              # 日志配置
│   └── test/                                   # 测试代码
│       └── java/com/example/ai/
│           ├── AiJavaDemoApplicationTests.java
│           ├── controller/
│           │   └── AiControllerTest.java
│           ├── service/
│           │   └── AiServiceTest.java
│           └── config/
│               └── WebClientConfigTest.java
├── pom.xml                                     # Maven配置
└── README.md                                   # 项目文档
```

## 快速开始

### 前置要求

- JDK 17 或更高版本
- Maven 3.6+
- 阿里云百炼 API Key

### 配置

1. **克隆项目**

```bash
git clone <repository-url>
cd ai-java-demo
```

2. **配置 API Key**

编辑 `src/main/resources/application.yml`，替换 API Key：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-actual-api-key-here
      agent:
        app-id: your-app-id-here

dashscope:
  api-key: your-actual-api-key-here
```

**推荐方式**：使用环境变量

```bash
export DASHSCOPE_API_KEY=your-api-key
export APP_ID=your-app-id
```

3. **构建项目**

```bash
mvn clean install
```

4. **运行应用**

```bash
mvn spring-boot:run
```

或直接运行：

```bash
java -jar target/ai-java-demo-0.0.1-SNAPSHOT.jar
```

应用将在 `http://localhost:9000` 启动。

## API 接口

### 1. AI 问答接口（GET）

**旧版兼容接口**

```bash
curl "http://localhost:9000/api/ai/ask?question=你好"
```

### 2. AI 问答接口（POST）

```bash
curl -X POST http://localhost:9000/api/ai/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "介绍一下Spring Boot",
    "model": "qwen-max"
  }'
```

### 3. 异步 AI 问答接口

```bash
curl -X POST http://localhost:9000/api/ai/ask/async \
  -H "Content-Type: application/json" \
  -d '{
    "question": "什么是机器学习?"
  }'
```

### 4. 百炼 Agent 流式对话

```bash
curl "http://localhost:9000/api/ai/bailian/agent/stream?message=你好"
```

### 5. 百炼 Agent 同步调用

```bash
curl "http://localhost:9000/api/ai/bailian/agent/call?message=如何使用SDK快速调用阿里云百炼的应用?"
```

## 统一响应格式

所有 API 返回统一的 JSON 格式：

```json
{
  "code": 200,
  "message": "success",
  "data": "响应数据"
}
```

错误响应：

```json
{
  "code": 500,
  "message": "错误信息",
  "data": null
}
```

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=AiServiceTest

# 查看测试覆盖率
mvn clean test jacoco:report
```

## 日志

日志文件位置：`logs/`

- `app.log` - 应用日志
- `error.log` - 错误日志

日志配置文件：`src/main/resources/log4j2-spring.xml`

## 监控端点

应用集成了 Spring Boot Actuator，可以访问以下监控端点：

- Health: `http://localhost:9000/actuator/health`
- Info: `http://localhost:9000/actuator/info`
- Metrics: `http://localhost:9000/actuator/metrics`

## 环境变量配置

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `SERVER_PORT` | 服务端口 | 9000 |
| `DASHSCOPE_API_KEY` | DashScope API密钥 | - |
| `APP_ID` | 百炼应用ID | - |
| `DASHSCOPE_DEFAULT_MODEL` | 默认AI模型 | qwen-max |
| `DASHSCOPE_TIMEOUT` | 请求超时时间（秒） | 60 |

## 开发指南

### 添加新的 AI 功能

1. 在 `service` 包中创建服务类
2. 在 `controller` 包中创建控制器
3. 添加相应的 DTO 类
4. 编写单元测试和集成测试

### 自定义异常处理

在 `GlobalExceptionHandler` 中添加新的异常处理方法：

```java
@ExceptionHandler(YourException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ApiResponse<Void> handleYourException(YourException e) {
    log.error("Your error: ", e);
    return ApiResponse.error(400, e.getMessage());
}
```

## 常见问题

### 1. API Key 配置错误

确保在 `application.yml` 或环境变量中正确配置了 API Key。

### 2. 端口冲突

修改 `application.yml` 中的 `server.port` 或使用环境变量 `SERVER_PORT`。

### 3. 依赖下载失败

```bash
mvn clean install -U
```

## 安全建议

⚠️ **重要**: 

- **不要**将 API Key 提交到版本控制系统
- 使用环境变量或密钥管理服务管理敏感信息
- 在生产环境中启用 HTTPS
- 定期轮换 API Key

## 贡献

欢迎提交 Issue 和 Pull Request！

## 许可证

本项目采用 MIT 许可证。

## 联系方式

- 作者: linzhang
- 项目地址: [GitHub Repository URL]

## 更新日志

### v0.0.1 (2026-01-28)

- ✨ 初始版本
- ✅ 集成阿里云百炼 AI
- ✅ 实现标准三层架构
- ✅ 添加统一异常处理
- ✅ 完善日志配置
- ✅ 添加单元测试和集成测试
