# AI Support Chatbot (Java + Maven)

An AI-powered customer support chatbot with a console UI and optional REST API. Built with Java 17+, Maven, SLF4J/Logback, and OpenAI integration.

## Requirements
- Java 17+
- Maven 3.8+
- OpenAI API key (configured)
- IntelliJ IDEA (recommended)

## Project Structure
```
Chatbot/
  src/main/java/com/example/chatbot/...    # Source code
  src/main/resources/                      # Resources (faq, logback)
  src/test/java/...                        # Unit tests
  .env                                     # API keys (configured)
  pom.xml                                  # Maven build
```

## Setup (IntelliJ)
1. **Open Project**: File → Open → select this folder (`Chatbot`)
2. **Import Dependencies**: Wait for Maven to import all dependencies
3. **Run Configuration**: 
   - Console: `com.example.chatbot.App`
   - REST API: `com.example.chatbot.api.SpringBootLauncher`

## API Configuration ✅
Your OpenAI API key is already configured in `.env`. The chatbot will use:
- **Model**: GPT-3.5 Turbo
- **Fallback**: Local FAQ + heuristics if API fails

## Build & Run

### Build the Project
```bash
mvn clean package
```
This creates: `./target/chatbot-1.0.0-shaded.jar`

### Run Console Version
```bash
java -jar target/chatbot-1.0.0-shaded.jar
```

### Run REST API Version
```bash
mvn spring-boot:run -Dspring-boot.run.main-class=com.example.chatbot.api.SpringBootLauncher
```
API available at: `http://localhost:8080`

Test with: `POST /api/chat`
```json
{
  "message": "Where is my order 12345?",
  "sessionId": "optional-session-id"
}
```

## Features Included
- ✅ **AI Integration**: OpenAI GPT-3.5 with intelligent fallback
- ✅ **Intent Detection**: Order status, refunds, product info, scheduling
- ✅ **Entity Extraction**: Order IDs, dates, product keywords
- ✅ **Knowledge Base**: Local FAQ from JSON file
- ✅ **Mock Services**: CRM, Orders, Payments integration
- ✅ **Automation**: Scheduling, follow-ups, issue logging
- ✅ **Session Management**: Context-aware conversations
- ✅ **Metrics**: Response times, resolution rates
- ✅ **Logging**: SLF4J + Logback for debugging
- ✅ **Security**: Input sanitization and error handling

## Test the Chatbot
Try these sample queries:
- `"Where is my order 12345?"`
- `"I want a refund for order 67890"`
- `"Tell me about your shipping policy"`
- `"Schedule a callback for tomorrow at 3pm"`
- `"What products do you sell?"`

## Docker Deployment (Optional)
```bash
docker build -t ai-chatbot .
docker run --rm -it -p 8080:8080 --env-file .env ai-chatbot
```

## Run Tests
```bash
mvn test
```

## IntelliJ Setup Tips
1. **Import as Maven Project**: IntelliJ should auto-detect Maven
2. **Java SDK**: Ensure Java 17+ is selected in Project Structure
3. **Run Configurations**: 
   - Main class: `com.example.chatbot.App` (console)
   - Main class: `com.example.chatbot.api.SpringBootLauncher` (REST)
4. **Environment Variables**: Set in Run Configuration if not using `.env`

## License
MIT
