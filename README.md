# tripCalc

A REST API backend for calculating and splitting trip expenses among a group of people.

## Running the App

Open the project in IntelliJ and run `TripCalcApplication.java`, or from the terminal:

```bash
./mvnw spring-boot:run
```

The API will start at `http://localhost:8080`.  
The H2 database console is available at `http://localhost:8080/h2-console`.

## API Endpoints

### Trips
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trips` | Create a trip |
| GET | `/api/trips` | Get all trips |
| GET | `/api/trips/{id}` | Get a trip by ID |
| GET | `/api/trips/{id}/settlement` | Get who owes who |

### People
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trips/{tripId}/people` | Add a person to a trip |
| GET | `/api/trips/{tripId}/people` | Get all people on a trip |

### Expenses
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trips/{tripId}/expenses` | Add an expense |
| GET | `/api/trips/{tripId}/expenses` | Get all expenses for a trip |
| GET | `/api/trips/{tripId}/expenses/person/{personId}` | Get expenses paid by a person |

## Example Usage (Windows CMD)

**1. Create a trip**
```cmd
curl -X POST http://localhost:8080/api/trips -H "Content-Type: application/json" -d "{\"name\": \"Paris Trip\"}"
```

**2. Add people**
```cmd
curl -X POST http://localhost:8080/api/trips/1/people -H "Content-Type: application/json" -d "{\"name\": \"Alice\"}"
curl -X POST http://localhost:8080/api/trips/1/people -H "Content-Type: application/json" -d "{\"name\": \"Bob\"}"
curl -X POST http://localhost:8080/api/trips/1/people -H "Content-Type: application/json" -d "{\"name\": \"Carol\"}"
```

**3. Add an expense**
```cmd
curl -X POST http://localhost:8080/api/trips/1/expenses -H "Content-Type: application/json" -d "{\"paidById\": 1, \"amount\": 60.00, \"description\": \"Dinner\", \"splitAmongIds\": [1,2,3]}"
```

**4. See who owes who**
```cmd
curl http://localhost:8080/api/trips/1/settlement
```

Example response:
```json
{
  "tripId": 1,
  "tripName": "Paris Trip",
  "settlements": [
    "Bob owes Alice €20.00",
    "Carol owes Alice €20.00"
  ]
}
```

## Running Tests

Run all tests from IntelliJ by right-clicking the `test` folder and selecting **Run All Tests**, or from the terminal:

```bash
./mvnw test
```

Tests cover:
- `TripServiceTest` — trip creation and retrieval
- `PersonServiceTest` — adding and fetching people
- `SettlementServiceTest` — settlement calculation logic
- `TripControllerTest` — HTTP endpoint responses
