# COMP2042 Tetris Refactoring & Enhancement Project

## Overview
This document tracks all refactoring work and new features added to the Tetris game codebase for COMP2042 coursework. The project demonstrates application of SOLID principles, design patterns, and object-oriented concepts while adding meaningful gameplay enhancements.

---

## 1. Refactoring: Meaningful Package Naming/Organisation

### Changes Made
Reorganized the entire codebase from a flat structure (`com.comp2042.*`) into a feature-focused package hierarchy:

#### New Package Structure:
```
com.comp2042/
├── app/
│   └── Main.java                    # Application entry point
├── ui/
│   ├── GuiController.java            # Main UI controller
│   ├── GameOverPanel.java           # Game over UI component
│   ├── NotificationPanel.java       # Score/notification display
│   ├── InputHandler.java            # Keyboard input handling
│   ├── GameTimer.java               # Game loop timing
│   ├── GameViewModel.java           # View model for rendering
│   ├── ColorMapper.java             # Color mapping utility
│   └── color/                       # Color strategy implementations
│       ├── ColorStrategy.java
│       ├── ColorStrategyRegistry.java
│       └── [8 color strategy classes]
├── game/
│   ├── board/                       # Board management
│   │   ├── Board.java              # Main board interface
│   │   ├── SimpleBoard.java        # Concrete board implementation
│   │   ├── MovableBoard.java       # Movement interface
│   │   ├── ClearableBoard.java     # Row clearing interface
│   │   ├── SpawnableBoard.java     # Brick spawning interface
│   │   └── ScorableBoard.java      # Scoring interface
│   ├── bricks/                      # Tetromino pieces
│   │   ├── Brick.java               # Brick interface
│   │   ├── BrickGenerator.java     # Generator interface
│   │   ├── BrickGeneratorFactory.java
│   │   ├── RandomBrickGenerator.java
│   │   └── [7 brick type classes: I, J, L, O, S, T, Z]
│   ├── controller/                  # Game control logic
│   │   ├── GameController.java     # Main game controller
│   │   └── commands/                # Command pattern implementations
│   │       ├── MoveCommand.java
│   │       ├── DownMoveCommand.java
│   │       ├── LeftMoveCommand.java
│   │       ├── RightMoveCommand.java
│   │       └── RotateMoveCommand.java
│   ├── data/                        # Data transfer objects
│   │   ├── ViewData.java
│   │   ├── DownData.java
│   │   ├── ClearRow.java
│   │   └── NextShapeInfo.java
│   ├── events/                      # Event handling
│   │   ├── InputEventListener.java
│   │   ├── MoveEvent.java
│   │   ├── EventType.java
│   │   └── EventSource.java
│   ├── operations/                  # Utility operations
│   │   ├── MatrixOperations.java
│   │   └── BrickRotator.java
│   ├── score/                       # Scoring system
│   │   └── Score.java
│   └── level/                       # Level system (NEW FEATURE)
│       ├── Level.java
│       ├── LevelManager.java
│       ├── LevelStrategy.java
│       ├── DefaultLevelStrategy.java
│       └── LinesClearedTracker.java
└── resources/
    ├── gameLayout.fxml
    ├── window_style.css
    ├── background_image.png
    └── digital.ttf
```

### Benefits
- **Improved Navigation**: Clear separation of concerns makes code easier to locate
- **Access Control**: Package-private visibility can be used effectively
- **Maintainability**: Related classes are grouped together
- **Scalability**: Easy to add new features in appropriate packages

### Files Modified
- All Java files moved to new package locations
- All import statements updated
- `pom.xml` updated with new main class path: `com.comp2042.app.Main`
- FXML references updated to reflect new controller package

---

## 2. Refactoring: Basic Maintenance

### 2.1 Method Renaming for Clarity
**Changed**: `createNewBrick()` → `trySpawnNewBrick()`

**Reason**: The original method name was misleading. The method returns `true` when a collision occurs (indicating failure), not success. The new name clearly indicates it attempts to spawn a brick and may fail.

**Impact**:
- Updated `Board` interface declaration
- Updated `SimpleBoard` implementation
- Updated `GameController` calls (constructor and game loop)
- Updated internal `SimpleBoard.newGame()` method

### 2.2 Encapsulation Improvements
- Made internal state private where appropriate
- Added proper getter methods for controlled access
- Ensured data immutability in DTOs (ViewData, DownData, etc.)

### 2.3 Resource Cleanup
- Verified all resources are properly used
- Removed any unused imports
- Ensured FXML and CSS resources are correctly referenced

---

## 3. Refactoring: Supporting Single Responsibility by Splitting Up Classes

### 3.1 GuiController Decomposition
**Problem**: `GuiController` was handling too many responsibilities:
- Font loading
- Key event routing
- Animation timing
- Board rendering
- Notification management
- Color mapping

**Solution**: Extracted responsibilities into focused classes:

#### 3.1.1 InputHandler
**File**: `com.comp2042.ui.InputHandler.java`

**Responsibility**: Handles all keyboard input events
- Processes arrow keys (LEFT, RIGHT, UP, DOWN)
- Handles WASD alternative controls
- Manages pause/game over state checks
- Delegates to event listener callbacks

**Benefits**:
- Input logic isolated and testable
- Can be easily modified without touching UI code
- Follows Single Responsibility Principle

#### 3.1.2 GameTimer
**File**: `com.comp2042.ui.GameTimer.java`

**Responsibility**: Manages game loop timing
- Controls automatic piece dropping
- Handles timer start/stop/pause
- Adjusts speed based on level

**Benefits**:
- Timing logic separated from UI
- Easy to modify drop speed behavior
- Can be tested independently

#### 3.1.3 GameViewModel
**File**: `com.comp2042.ui.GameViewModel.java`

**Responsibility**: Handles data-to-UI transformations
- Positions brick panels
- Updates rectangle colors
- Calculates ghost piece positions
- Transforms game state to visual representation

**Benefits**:
- Rendering logic separated from controller
- Can be swapped for different view implementations
- Easier to test rendering logic

#### 3.1.4 ColorMapper & ColorStrategy
**Files**: 
- `com.comp2042.ui.ColorMapper.java`
- `com.comp2042.ui.color.ColorStrategy.java` (interface)
- `com.comp2042.ui.color.ColorStrategyRegistry.java`
- 8 concrete color strategy classes

**Responsibility**: Maps color codes to Paint objects
- Replaced switch statement with Strategy pattern
- Each color has its own strategy class
- Registry manages strategy selection

**Benefits**:
- Eliminates large switch statement
- New colors can be added without modifying existing code
- Demonstrates Open/Closed Principle

### 3.2 Board Interface Segregation
**Problem**: `Board` interface had too many responsibilities

**Solution**: Segregated into focused interfaces:

#### Segregated Interfaces:
- **`MovableBoard`**: Movement operations (left, right, down, rotate)
- **`ClearableBoard`**: Row clearing operations
- **`SpawnableBoard`**: Brick spawning operations
- **`ScorableBoard`**: Score access

**Main Interface**:
```java
public interface Board extends MovableBoard, ClearableBoard, 
                              SpawnableBoard, ScorableBoard {
    // All methods inherited from segregated interfaces
}
```

**Benefits**:
- Clients depend only on interfaces they need (Interface Segregation Principle)
- Easier to create mock boards for testing
- Clear separation of concerns
- More flexible for future board implementations

---

## 4. Refactoring: Design Patterns

### 4.1 Strategy Pattern

#### 4.1.1 Color Strategy
**Implementation**: `com.comp2042.ui.color.ColorStrategy`

**Purpose**: Replace switch statement for color mapping with polymorphic strategy selection

**Structure**:
- **Interface**: `ColorStrategy` with `getColor()` method
- **Concrete Strategies**: 8 classes (AquaColorStrategy, RedColorStrategy, etc.)
- **Registry**: `ColorStrategyRegistry` manages strategy selection
- **Context**: `ColorMapper` uses registry to get appropriate strategy

**Benefits**:
- Open/Closed Principle: New colors added without modifying existing code
- Eliminates large switch statement
- Each color strategy is independently testable

#### 4.1.2 Level Strategy
**Implementation**: `com.comp2042.game.level.LevelStrategy`

**Purpose**: Allow different level progression schemes to be swapped

**Structure**:
- **Interface**: `LevelStrategy` with `getLevel(int levelNumber)` method
- **Concrete Strategy**: `DefaultLevelStrategy` provides predefined level configurations
- **Context**: `LevelManager` uses strategy to retrieve level configurations

**Benefits**:
- Easy to add new level progression schemes (e.g., exponential, custom difficulty curves)
- Level configuration logic separated from level management
- Testable in isolation

### 4.2 Factory Pattern

#### 4.2.1 BrickGeneratorFactory
**Implementation**: `com.comp2042.game.bricks.BrickGeneratorFactory`

**Purpose**: Decouple `SimpleBoard` from concrete `RandomBrickGenerator` implementation

**Structure**:
- **Factory Class**: `BrickGeneratorFactory` with `createDefault()` method
- **Product Interface**: `BrickGenerator`
- **Concrete Product**: `RandomBrickGenerator`

**Benefits**:
- Dependency Inversion: Board depends on interface, not concrete class
- Easy to swap generators (e.g., bag system, debug sequences)
- Better testability (can inject mock generators)

### 4.3 Command Pattern

**Implementation**: `com.comp2042.game.controller.commands.MoveCommand`

**Purpose**: Encapsulate move operations as objects, eliminating nested conditionals

**Structure**:
- **Command Interface**: `MoveCommand` with `execute()` method
- **Concrete Commands**:
  - `LeftMoveCommand`
  - `RightMoveCommand`
  - `RotateMoveCommand`
  - `DownMoveCommand` (encapsulates complex landing logic)

**Benefits**:
- Eliminates nested conditionals in `GameController`
- Each command is independently testable
- Easy to add new move types (e.g., hard drop)
- Commands can be queued, undone, or logged
- Open/Closed Principle: New commands added without modifying controller

**Example**:
```java
// Before: Nested conditionals in GameController
if (event.getType() == EventType.LEFT) {
    // left movement logic
} else if (event.getType() == EventType.RIGHT) {
    // right movement logic
}

// After: Polymorphic command execution
MoveCommand command = new LeftMoveCommand(board);
command.execute();
```

### 4.4 Observer Pattern

**Implementation**: JavaFX `IntegerProperty` for reactive updates

**Purpose**: Enable automatic UI updates when game state changes

**Usage**:
- **Score**: `Score.scoreProperty()` bound to UI label
- **Level**: `LevelManager.levelProperty()` bound to UI label
- **Lines Cleared**: `LinesClearedTracker.linesProperty()` observed by `LevelManager`

**Benefits**:
- Automatic UI synchronization
- Loose coupling: Score/Level don't need to know about UI
- Reactive updates without manual refresh calls

### 4.5 Dependency Injection

**Implementation**: Constructor injection in `GameController` and `SimpleBoard`

**Purpose**: Reduce coupling and improve testability

**Examples**:
- `GameController` accepts `Board` interface (not concrete `SimpleBoard`)
- `SimpleBoard` accepts `BrickGeneratorFactory` (via factory method)
- `LevelManager` accepts `LinesClearedTracker` and `LevelStrategy`

**Benefits**:
- Dependency Inversion Principle: Depend on abstractions
- Easy to inject mocks for testing
- Flexible: Can swap implementations without code changes

---

## 5. Refactoring: Meaningful JUnit Tests

### Test Structure
All tests located in `src/test/java/com/comp2042/` mirroring main package structure.

### Test Files Created:

#### 5.1 MatrixOperationsTest
**File**: `src/test/java/com/comp2042/game/operations/MatrixOperationsTest.java`

**Coverage**:
- `intersect()`: Collision detection logic
- `merge()`: Merging brick into board matrix
- `checkRemoving()`: Row clearing detection and removal
- `copy()`: Matrix copying functionality

**Importance**: These operations are critical for gameplay correctness - bugs here cause visual glitches and gameplay issues.

#### 5.2 ScoreTest
**File**: `src/test/java/com/comp2042/game/score/ScoreTest.java`

**Coverage**:
- Score increment logic
- Score property binding
- Score reset functionality

#### 5.3 BrickRotatorTest
**File**: `src/test/java/com/comp2042/game/bricks/BrickRotatorTest.java`

**Coverage**:
- Rotation logic for all brick types
- Shape transformation correctness
- Next shape calculation

#### 5.4 RandomBrickGeneratorTest
**File**: `src/test/java/com/comp2042/game/bricks/RandomBrickGeneratorTest.java`

**Coverage**:
- Random brick generation
- Next brick preview functionality
- Generator state management

#### 5.5 BrickGeneratorFactoryTest
**File**: `src/test/java/com/comp2042/game/bricks/BrickGeneratorFactoryTest.java`

**Coverage**:
- Factory creation method
- Returned generator type
- Factory pattern correctness

### Test Configuration
- **Framework**: JUnit 5 (Jupiter)
- **Maven Plugin**: `maven-surefire-plugin` configured for JUnit 5
- **Run Command**: `./mvnw test`

---

## 6. Refactoring: Fixing Bugs

### 6.1 Input Latency Fix
**Problem**: Significant delay between key press and piece movement/rotation

**Root Cause**: `InputHandler` was calling event listeners but not immediately refreshing the UI. The `ViewData` returned from events was being ignored.

**Solution**: 
- Added callback parameter `Consumer<ViewData> onBrickMoved` to `InputHandler`
- `InputHandler` now captures returned `ViewData` and immediately calls callback
- `GuiController` passes `this::refreshBrick` as callback

**Result**: Immediate UI updates on key press, eliminating latency.

### 6.2 Score Binding Fix
**Problem**: Score was never displayed in UI despite `Score` class exposing `IntegerProperty`

**Solution**:
- Added `scoreLabel` to FXML layout
- Implemented `bindScore()` method in `GuiController`
- Bound score property to label using JavaFX bindings

**Result**: Score now displays and updates automatically.

### 6.3 Method Naming Clarification
**Problem**: `createNewBrick()` returned `true` on failure (collision), which was counterintuitive

**Solution**: Renamed to `trySpawnNewBrick()` to clearly indicate it's an attempt that may fail

**Result**: Code is more readable and intention-revealing.

---

## 7. New Features: Level System

### 7.1 Overview
Implemented a comprehensive level progression system with adaptive difficulty. Players advance levels every 5 lines cleared, with increasing drop speed and score multipliers.

### 7.2 Components

#### 7.2.1 Level
**File**: `com.comp2042.game.level.Level.java`

**Purpose**: Immutable configuration object representing a level

**Properties**:
- `levelNumber`: Current level (1-10+)
- `dropSpeedMs`: Automatic drop speed in milliseconds (400ms → 60ms)
- `scoreMultiplier`: Score multiplier for line clears (1.0x → 2.5x)

**Design**: Immutable class ensuring thread safety and predictable behavior.

#### 7.2.2 LinesClearedTracker
**File**: `com.comp2042.game.level.LinesClearedTracker.java`

**Purpose**: Tracks total lines cleared during the game

**Features**:
- Uses JavaFX `IntegerProperty` for reactive updates
- `addLines(int lines)`: Increments total
- `reset()`: Resets to zero on new game
- `linesProperty()`: Exposes property for binding

**Design Pattern**: Observer Pattern (via JavaFX properties)

#### 7.2.3 LevelStrategy
**File**: `com.comp2042.game.level.LevelStrategy.java`

**Purpose**: Strategy interface for level configuration retrieval

**Design Pattern**: Strategy Pattern

**Benefits**: Allows different progression schemes (linear, exponential, custom) without modifying core logic.

#### 7.2.4 DefaultLevelStrategy
**File**: `com.comp2042.game.level.DefaultLevelStrategy.java`

**Purpose**: Default implementation providing predefined level configurations

**Level Configurations**:
- Level 1: 400ms drop speed, 1.0x multiplier
- Level 2: 350ms drop speed, 1.2x multiplier
- Level 3: 300ms drop speed, 1.5x multiplier
- Level 4: 250ms drop speed, 1.8x multiplier
- Level 5: 200ms drop speed, 2.0x multiplier
- Level 6: 150ms drop speed, 2.2x multiplier
- Level 7: 120ms drop speed, 2.3x multiplier
- Level 8: 100ms drop speed, 2.4x multiplier
- Level 9: 80ms drop speed, 2.5x multiplier
- Level 10+: 60ms drop speed, 2.5x multiplier (capped)

#### 7.2.5 LevelManager
**File**: `com.comp2042.game.level.LevelManager.java`

**Purpose**: Manages level progression based on lines cleared

**Features**:
- Tracks current level
- Updates level every 5 lines cleared
- Exposes level property for UI binding
- Provides current level configuration

**Integration**:
- Observes `LinesClearedTracker` for line count changes
- Notifies `GuiController` on level up
- Used by `DownMoveCommand` to apply score multipliers

### 7.3 Integration Points

#### 7.3.1 SimpleBoard
- Initializes `LinesClearedTracker` and `LevelManager`
- Resets both on `newGame()`
- Provides accessors for `LevelManager`

#### 7.3.2 DownMoveCommand
- Tracks lines cleared via `LinesClearedTracker`
- Updates level via `LevelManager.updateLevel()`
- Applies level-based score multiplier
- Triggers `GuiController.onLevelUp()` notification

#### 7.3.3 GameController
- Binds `LevelManager` to `GuiController` if board is `SimpleBoard`
- Enables level display in UI

#### 7.3.4 GuiController
- Displays current level in UI label
- Shows "LEVEL X!" notification on level up
- Updates game timer speed based on current level
- Binds level property to label for automatic updates

### 7.4 UI Changes
- Added `levelLabel` to `gameLayout.fxml`
- Level displayed as "Level: X" next to score
- Level-up notifications appear similar to score notifications

### 7.5 Design Benefits
- **Strategy Pattern**: Easy to swap level progression schemes
- **Observer Pattern**: Automatic UI updates via property binding
- **Single Responsibility**: Each component has one clear purpose
- **Dependency Inversion**: `LevelManager` depends on `LevelStrategy` interface
- **Encapsulation**: Level configuration encapsulated in immutable `Level` objects

---

## 8. New Features: Ghost Piece

### 8.1 Overview
Ghost piece feature shows players where their current piece will land, helping with placement decisions. The ghost appears as a semi-transparent preview (30% opacity) of the piece at its landing position, updating in real-time as the player moves or rotates the piece.

### 8.2 Components

#### 8.2.1 ViewData Enhancement
**File**: `com.comp2042.game.data.ViewData.java`

**Changes**:
- Added `ghostX` and `ghostY` fields to store ghost piece position
- Added `getGhostX()` and `getGhostY()` accessor methods
- Updated constructor to accept ghost position parameters

**Design**: Ghost position calculated in `SimpleBoard` and passed through `ViewData` to UI layer, maintaining separation of concerns.

#### 8.2.2 SimpleBoard Ghost Calculation
**File**: `com.comp2042.game.board.SimpleBoard.java`

**New Method**: `findDropY(int startX, int startY)`

**Purpose**: Calculates where the piece will land by simulating it falling down

**Algorithm**:
1. Start from current piece position
2. Increment Y position downward
3. Check for collision using `MatrixOperations.intersect()`
4. Stop when collision detected
5. Return the landing Y position

**Integration**:
- Called in `getViewData()` method
- Ghost X matches current piece X (only Y changes)
- Ghost Y calculated via `findDropY()`
- Both passed to `ViewData` constructor

**Code Example**:
```java
private int findDropY(int startX, int startY) {
    int[][] shape = brickRotator.getCurrentShape();
    int ghostY = startY;
    
    // Loop downward until collision detected
    while (!MatrixOperations.intersect(currentGameMatrix, shape, startX, ghostY + 1)) {
        ghostY++;
    }
    
    return ghostY;
}
```

#### 8.2.3 ColorMapper Ghost Support
**File**: `com.comp2042.ui.ColorMapper.java`

**New Method**: `getGhostFillColor(int code)`

**Purpose**: Returns semi-transparent version of piece colors for ghost rendering

**Implementation**:
- Retrieves solid color via `ColorStrategyRegistry`
- Converts to `Color` object if applicable
- Creates new `Color` with same RGB but 30% opacity (`GHOST_OPACITY = 0.3`)
- Returns transparent color for empty cells (code 0)

**Design**: Centralized ghost color logic ensures consistent visual style across all piece types.

#### 8.2.4 GuiController Ghost Rendering
**File**: `com.comp2042.ui.GuiController.java`

**UI Components**:
- `@FXML private GridPane ghostPanel`: Ghost piece panel (defined in FXML)
- `private Rectangle[][] ghostRectangles`: Array of rectangles for ghost piece rendering

**Initialization** (in `initGameView()`):
1. Creates ghost rectangles matching brick dimensions
2. Sets initial colors using `ColorMapper.getGhostFillColor()`
3. Adds rectangles to `ghostPanel`
4. Positions ghost panel at landing location

**Update Methods**:

**`positionGhostPanel(ViewData brick)`**:
- Positions ghost panel at `(ghostX, ghostY)` from `ViewData`
- Uses same positioning formula as active brick panel
- Updates whenever piece moves or rotates

**`updateGhostRectangles(ViewData brick)`**:
- Updates ghost rectangle colors based on current piece shape
- Only non-zero cells (actual piece blocks) are visible
- Empty cells remain transparent
- Maintains rounded corners (arcHeight/arcWidth = 9)

**Integration**:
- Called in `refreshBrick()` method after active brick updates
- Ghost updates automatically whenever piece moves/rotates
- Ghost position recalculated on every `getViewData()` call

#### 8.2.5 FXML Layout Update
**File**: `src/main/resources/gameLayout.fxml`

**Changes**:
- Added `<GridPane fx:id="ghostPanel" vgap="1" hgap="1"/>` to scene graph
- Positioned before `brickPanel` so active piece renders on top

### 8.3 Visual Design
- **Opacity**: 30% (0.3) for clear visibility without obscuring board
- **Positioning**: Same X as current piece, Y at landing position
- **Styling**: Rounded corners matching active piece style
- **Colors**: Semi-transparent versions of piece colors
- **Update Frequency**: Real-time updates on every move/rotation

### 8.4 Benefits
- **Improved Gameplay**: Players can see landing position before dropping
- **Better Placement**: Helps with strategic piece positioning
- **Visual Feedback**: Clear indication of where piece will land
- **Non-Intrusive**: Semi-transparent design doesn't obstruct view
- **Real-Time Updates**: Ghost position updates immediately on movement

### 8.5 Design Patterns & Principles
- **Separation of Concerns**: Ghost calculation in `SimpleBoard`, rendering in `GuiController`
- **Single Responsibility**: Each component handles one aspect (calculation, color, rendering)
- **Reusability**: `ColorMapper.getGhostFillColor()` used consistently
- **Encapsulation**: Ghost position encapsulated in `ViewData`
- **Observer Pattern**: Ghost updates automatically via `refreshBrick()` calls

---

## 9. Object-Oriented Concepts Demonstrated

### 9.1 Encapsulation
- Private fields with controlled access via getters
- Immutable DTOs (ViewData, Level)
- Internal state hidden from clients

### 9.2 Inheritance
- Brick classes extend/implement `Brick` interface
- Color strategies implement `ColorStrategy` interface
- Commands implement `MoveCommand` interface

### 9.3 Polymorphism
- Strategy pattern: Different strategies used polymorphically
- Command pattern: Commands executed polymorphically
- Interface segregation: Different board implementations

### 9.4 Abstraction
- Interfaces hide implementation details
- Abstract concepts (Board, Brick, Strategy) separate from concrete implementations

### 9.5 Composition
- `SimpleBoard` composes `BrickGenerator`, `BrickRotator`, `Score`, `LevelManager`
- `LevelManager` composes `LinesClearedTracker` and `LevelStrategy`
- Components work together without tight coupling

---

## 10. SOLID Principles Applied

### 10.1 Single Responsibility Principle (SRP)
- Each class has one reason to change
- Examples: `InputHandler` (input only), `GameTimer` (timing only), `GameViewModel` (rendering only)

### 10.2 Open/Closed Principle (OCP)
- Open for extension, closed for modification
- Examples: New colors via `ColorStrategy`, new commands via `MoveCommand`, new level strategies via `LevelStrategy`

### 10.3 Liskov Substitution Principle (LSP)
- Subtypes must be substitutable for their base types
- Examples: Any `Brick` implementation works, any `LevelStrategy` works, any `ColorStrategy` works

### 10.4 Interface Segregation Principle (ISP)
- Clients shouldn't depend on interfaces they don't use
- Examples: Segregated `Board` interfaces (`MovableBoard`, `ClearableBoard`, etc.)

### 10.5 Dependency Inversion Principle (DIP)
- Depend on abstractions, not concretions
- Examples: `GameController` depends on `Board` interface, `SimpleBoard` depends on `BrickGenerator` interface, `LevelManager` depends on `LevelStrategy` interface

---

## 11. Build & Run Instructions

### Prerequisites
- Java 23
- Maven (or use included Maven wrapper)

### Build
```bash
./mvnw clean compile
```

### Run
```bash
./mvnw clean javafx:run
```

### Test
```bash
./mvnw test
```

### Generate Javadoc
```bash
./mvnw javadoc:javadoc
```

---

## 12. Git History

### Branch Strategy
- `master`: Stable, demoable code
- Feature branches used for major features (e.g., `Additional-Features`)

### Key Commits
1. **Package reorganization**: Moved all classes to feature-focused packages
2. **Basic maintenance**: Method renaming, encapsulation improvements
3. **Single responsibility**: Extracted InputHandler, GameTimer, GameViewModel, ColorMapper
4. **Design patterns**: Implemented Strategy, Factory, Command, Observer patterns
5. **JUnit tests**: Added comprehensive test suite
6. **Bug fixes**: Fixed input latency, score binding, method naming
7. **Level system**: Implemented adaptive difficulty with Strategy pattern
8. **Ghost piece**: Implemented semi-transparent landing preview with real-time updates

---

## 13. Future Enhancements

### Potential Features
- **Next Piece Preview**: Display upcoming piece (data already available in ViewData)
- **Hold Piece**: Allow players to hold/swap current piece
- **Hard Drop**: Instant drop with spacebar + bonus points
- **Combo System**: Bonus points for consecutive line clears
- **High Score Persistence**: Save/load high scores
- **Sound Effects**: Audio feedback for actions

### Code Improvements
- Additional unit tests for new features
- Integration tests for game flow
- Performance optimizations
- Additional design pattern applications

---

## 14. Summary

This project demonstrates:
- ✅ Meaningful package organization
- ✅ Basic maintenance (renaming, encapsulation, cleanup)
- ✅ Single Responsibility Principle through class decomposition
- ✅ Multiple design patterns (Strategy, Factory, Command, Observer)
- ✅ Comprehensive JUnit test suite
- ✅ Bug fixes improving gameplay experience
- ✅ New gameplay features:
  - **Level System**: Adaptive difficulty with Strategy pattern (every 5 lines = new level)
  - **Ghost Piece**: Semi-transparent landing preview with real-time updates
- ✅ SOLID principles throughout
- ✅ Object-oriented concepts (Encapsulation, Inheritance, Polymorphism, Abstraction, Composition)

All changes maintain backward compatibility while significantly improving code quality, maintainability, and gameplay experience.

