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
│   │       ├── RotateMoveCommand.java
│   │       └── HardDropMoveCommand.java
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
- All Java files moved to new package locations (31 files reorganized)
- All import statements updated
- `pom.xml` updated with new main class path: `com.comp2042.app.Main`
- FXML references updated: `fx:controller="com.comp2042.ui.GuiController"`

### Commit Details
- **Commit**: `732b2d7`
- **Date**: October 28, 2025
- **Message**: "refactor: reorganize packages for better structure"

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
- 9 concrete color strategy classes:
  - `TransparentColorStrategy.java` (code 0)
  - `AquaColorStrategy.java` (code 1)
  - `BlueVioletColorStrategy.java` (code 2)
  - `DarkGreenColorStrategy.java` (code 3)
  - `YellowColorStrategy.java` (code 4)
  - `RedColorStrategy.java` (code 5)
  - `BeigeColorStrategy.java` (code 6)
  - `BurlyWoodColorStrategy.java` (code 7)
  - `WhiteColorStrategy.java` (default/fallback)

**Responsibility**: Maps color codes to Paint objects using polymorphism
- Replaced 10-case switch statement with Strategy pattern
- Each color code has its own strategy class implementing `ColorStrategy` interface
- `ColorStrategyRegistry` uses HashMap for O(1) strategy lookup
- Registry pattern provides centralized management

**Implementation Details**:
```java
// Strategy interface
public interface ColorStrategy {
    int getColorCode();
    Paint getColor();
}

// Registry manages all strategies
public class ColorStrategyRegistry {
    private static final Map<Integer, ColorStrategy> strategies = new HashMap<>();
    
    static {
        register(new TransparentColorStrategy());
        register(new AquaColorStrategy());
        // ... 7 more strategies
    }
    
    public static Paint getColor(int code) {
        ColorStrategy strategy = strategies.getOrDefault(code, new WhiteColorStrategy());
        return strategy.getColor(); // Polymorphic call
    }
}
```

**Benefits**:
- Eliminates large switch statement (replaced 10 cases with polymorphic dispatch)
- New colors added by creating new strategy class (no modification of existing code)
- Demonstrates Open/Closed Principle perfectly
- Registry Pattern provides centralized strategy management
- Each strategy is independently testable

### 3.2 Board Interface Segregation
**Problem**: `Board` interface had too many responsibilities (movement, clearing, spawning, scoring all in one interface)

**Solution**: Segregated into focused interfaces following Interface Segregation Principle:

#### Segregated Interfaces:

**`MovableBoard`** - Movement operations:
- `boolean moveBrickDown()`
- `boolean moveBrickLeft()`
- `boolean moveBrickRight()`
- `boolean rotateLeftBrick()`
- `ViewData getViewData()`

**`ClearableBoard`** - Row clearing operations:
- `void mergeBrickToBackground()`
- `ClearRow clearRows()`
- `int[][] getBoardMatrix()`

**`SpawnableBoard`** - Brick spawning operations:
- `boolean trySpawnNewBrick()`
- `void newGame()`

**`ScorableBoard`** - Score access:
- `Score getScore()`

**Main Interface** (Composition of segregated interfaces):
```java
public interface Board extends MovableBoard, ClearableBoard, 
                              SpawnableBoard, ScorableBoard {
    // All methods inherited from segregated interfaces
    // No additional methods defined here
}
```

**Benefits**:
- **Interface Segregation Principle**: Clients depend only on interfaces they need
  - Example: `InputHandler` only needs `MovableBoard` methods
  - Example: Row clearing logic only needs `ClearableBoard` methods
- Easier to create mock boards for testing (can mock only needed interfaces)
- Clear separation of concerns (each interface has single responsibility)
- More flexible for future board implementations
- Reduces coupling between components

### Commit Details
- **Commit**: `dec9e11`
- **Date**: October 30, 2025
- **Message**: "refactor(ui): enforce single-responsibility across UI components"
- **Files Changed**: 5 files (ColorMapper.java, GameTimer.java, GameViewModel.java, GuiController.java, InputHandler.java)

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
  - `HardDropMoveCommand` (instant drop to landing position)

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

**Implementation**: Constructor injection in `GameController`, `SimpleBoard`, and `LevelManager`

**Purpose**: Reduce coupling, improve testability, and enable flexible configuration

**Examples**:

**1. GameController - Board Injection**:
```java
// Before: Tight coupling to SimpleBoard
public class GameController {
    private SimpleBoard board = new SimpleBoard(25, 10);
}

// After: Depends on Board interface
public class GameController {
    private final Board board;
    
    public GameController(GuiController c, Board board) {
        this.board = board; // Injected via constructor
    }
}

// In Main.java - dependency is created and injected
Board board = new SimpleBoard(25, 10, generatorFactory);
new GameController(c, board);
```

**2. SimpleBoard - Factory Injection**:
```java
// Before: Direct instantiation of generator
public SimpleBoard(int width, int height) {
    this.brickGenerator = new RandomBrickGenerator(); // Tight coupling
}

// After: Uses factory (injected via method call)
public SimpleBoard(int width, int height) {
    this.brickGenerator = BrickGeneratorFactory.createDefault();
}
```

**3. LevelManager - Strategy and Tracker Injection**:
```java
public class LevelManager {
    private final LinesClearedTracker linesTracker;
    private final LevelStrategy levelStrategy;
    
    public LevelManager(LinesClearedTracker linesTracker, 
                       LevelStrategy levelStrategy) {
        this.linesTracker = linesTracker; // Injected
        this.levelStrategy = levelStrategy; // Injected
    }
}
```

**Benefits**:
- **Dependency Inversion Principle**: Depend on abstractions (interfaces), not concretions
- **Testability**: Easy to inject mocks for unit testing
- **Flexibility**: Can swap implementations without code changes
- **Loose Coupling**: Components don't create their dependencies
- **Configuration**: Easy to configure different setups (e.g., different generators, strategies)

### Commit Details
- **Commit**: `c577acc` (merge commit)
- **Date**: November 2, 2025
- **Message**: "refactor: implement dependency injection and factory patterns"
- **Files Changed**: 13 files including Main.java, GameController.java, SimpleBoard.java, BrickGeneratorFactory.java

---

## 5. Refactoring: Meaningful JUnit Tests

### Test Structure
All tests located in `src/test/java/com/comp2042/` mirroring main package structure.

### Test Coverage Summary
- **31 test files** created
- **189+ test methods** (`@Test` annotations)
- **Coverage areas**: Bricks, Operations, Score, Board, Controller, Commands, Data, Events, Level system
- **Test Framework**: JUnit 5 (Jupiter) with Mockito for mocking
- **Build Configuration**: Maven Surefire plugin configured for JUnit Platform
- **Run Command**: `./mvnw test`

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

#### 5.6 Individual Brick Tests
**Files**: `IBrickTest.java`, `JBrickTest.java`, `LBrickTest.java`, `OBrickTest.java`, `SBrickTest.java`, `TBrickTest.java`, `ZBrickTest.java`

**Coverage**:
- Shape correctness for each brick type
- Color codes match expected values
- Initial positions correct

#### 5.7 Controller and Command Tests
**Files**: `GameControllerTest.java`, `DownMoveCommandTest.java`, `LeftMoveCommandTest.java`, `RightMoveCommandTest.java`, `RotateMoveCommandTest.java`, `HardDropMoveCommandTest.java`

**Coverage**:
- Command execution correctness
- Move operations behavior
- Hard drop functionality (2 points per cell)
- Game controller event handling

#### 5.8 Board Tests
**Files**: `SimpleBoardTest.java`, `GhostBlockTest.java`

**Coverage**:
- Board operations (move, rotate, clear)
- Ghost piece calculation
- Collision detection
- Game state management

#### 5.9 Level System Tests
**Files**: `LevelTest.java`, `LevelManagerTest.java`, `DefaultLevelStrategyTest.java`, `LinesClearedTrackerTest.java`

**Coverage**:
- Level progression (every 5 lines)
- Score multipliers (1.0x to 2.5x)
- Drop speed scaling (400ms to 60ms)
- Lines tracking

#### 5.10 Data and Event Tests
**Files**: `ViewDataTest.java`, `DownDataTest.java`, `ClearRowTest.java`, `NextShapeInfoTest.java`, `MoveEventTest.java`, `EventTypeTest.java`, `EventSourceTest.java`

**Coverage**:
- Data object construction
- Immutability verification
- Event creation and properties

### Test Configuration
- **Framework**: JUnit 5 (Jupiter) - `junit-jupiter-engine:5.10.0`
- **Mocking**: Mockito - `mockito-core:5.14.2`, `mockito-junit-jupiter:5.14.2`
- **Maven Plugin**: `maven-surefire-plugin:3.5.2` configured for JUnit Platform
- **Compiler**: Java 23 with release flag
- **Run Command**: `./mvnw test`

### Commit Details
- **Initial Commit**: `c0e0ebc`
- **Date**: November 12, 2025
- **Message**: "test: add initial JUnit 5 suite (bricks, operations, score)"
- **Subsequent Commits**:
  - `a0b3ee4` (Nov 20): "test: add JUnit tests for ghost piece feature"
  - `12cb666` (Nov 20): "build: add Mockito dependencies for unit testing"
  - `6593775` (Nov 20): "test: add comprehensive JUnit test suite for game components"
  - `0419cd8` (Nov 25): "test: Add comprehensive tests for data package and HardDropMoveCommand"

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

### Commit Details
- **Commit**: `ce55a4d`
- **Date**: November 19, 2025
- **Message**: "fix: eliminate input latency by refreshing UI immediately after moves"

### 6.2 Score Binding Fix
**Problem**: Score was never displayed in UI despite `Score` class exposing `IntegerProperty`

**Solution**:
- Added `scoreLabel` to FXML layout
- Implemented `bindScore()` method in `GuiController`
- Bound score property to label using JavaFX bindings

**Result**: Score now displays and updates automatically.

### Commit Details
- **Commit**: `6b4f6c0`
- **Date**: October 28, 2025
- **Message**: "refactor: basic maintenance and bug fixes"
- **Files Changed**: 7 files (Board.java, SimpleBoard.java, GameController.java, MatrixOperations.java, GuiController.java, gameLayout.fxml, window_style.css)

### 6.3 Method Naming Clarification
**Problem**: `createNewBrick()` returned `true` on failure (collision), which was counterintuitive

**Solution**: Renamed to `trySpawnNewBrick()` to clearly indicate it's an attempt that may fail

**Result**: Code is more readable and intention-revealing.

---

## 7. New Features: Gameplay Controls

### 7.1 Hard Drop
**Implementation**: `HardDropMoveCommand` and `GameController.onHardDropEvent()`

**Purpose**: Allow players to instantly drop the current piece to its landing position (ghost position)

**Controls**: **Space** key

**Features**:
- Piece instantly moves to landing position (ghost position)
- Awards **2 points per cell dropped** as bonus
- Triggers normal landing logic (merge, clear rows, scoring)
- Applies level-based score multipliers to cleared lines
- Shows score notifications for cleared lines
- Updates ghost piece after drop

**Implementation Details**:
```java
public class HardDropMoveCommand implements MoveCommand {
    @Override
    public ViewData execute() {
        int cellsDropped = 0;
        
        // Move down until brick can't move anymore
        while (board.moveBrickDown()) {
            cellsDropped++;
        }
        
        // Award 2 points per cell dropped
        if (cellsDropped > 0) {
            board.getScore().add(cellsDropped * 2);
        }
        
        // Handle landing (merge, clear, spawn)
        handleBrickLanded();
        
        return board.getViewData();
    }
}
```

**Design**: 
- Uses Command Pattern (`HardDropMoveCommand`) to encapsulate instant drop logic
- Maintains consistency with other move commands (`LeftMoveCommand`, `RightMoveCommand`, etc.)
- Follows Single Responsibility - only handles hard drop operation
- Integrates with level system for score multipliers

### Commit Details
- **Commit**: `8b628d3`
- **Date**: November 20, 2025
- **Message**: "feat: add hard drop feature with SPACE bar"

### 7.2 Pause/Resume
**Implementation**: `GuiController.togglePause()`

**Purpose**: Allow players to pause and resume the game mid-play

**Controls**: **P** key

**Features**:
- Pauses game timer (stops automatic piece dropping)
- Shows "PAUSED" notification overlay on screen
- Prevents all movement input during pause (LEFT, RIGHT, UP, DOWN, SPACE blocked)
- Resumes with current level speed on unpause
- Cannot pause when game is over
- Pressing P again resumes the game

**Implementation Details**:
```java
public void togglePause() {
    if (isGameOver.getValue() == Boolean.TRUE) {
        return; // Don't allow pause when game is over
    }
    
    boolean currentPauseState = isPause.getValue();
    isPause.setValue(!currentPauseState);
    
    if (isPause.getValue()) {
        // Pausing
        gameTimer.stop();
        showPauseNotification();
    } else {
        // Resuming
        startTimerWithCurrentLevelSpeed();
        hidePauseNotification();
    }
}
```

**Design**: 
- Uses JavaFX `BooleanProperty` for pause state (reactive updates)
- Integrated with `InputHandler` for input blocking
- Integrated with `GameTimer` for timer control
- Clean state management without complex flags

### Commit Details
- **Commit**: `e55a350`
- **Date**: November 24, 2025
- **Message**: "refactor: Apply pause game functionality with P key"

### 7.3 Game Controls Summary

| Key | Action | Description |
|-----|--------|-------------|
| **LEFT** / **A** | Move Left | Move piece one cell left |
| **RIGHT** / **D** | Move Right | Move piece one cell right |
| **UP** / **W** | Rotate | Rotate piece 90° counterclockwise |
| **DOWN** / **S** | Soft Drop | Move piece down one cell (normal speed) |
| **SPACE** | Hard Drop | Instantly drop piece to landing position (+2 pts/cell) |
| **P** | Pause/Resume | Toggle pause state |
| **N** | New Game | Start a new game (anytime) |

---

## 8. New Features: Level System

### 8.1 Overview
Implemented a comprehensive level progression system with adaptive difficulty. Players advance levels every 5 lines cleared, with increasing drop speed and score multipliers.

### 8.2 Components

#### 8.2.1 Level
**File**: `com.comp2042.game.level.Level.java`

**Purpose**: Immutable configuration object representing a level

**Properties**:
- `levelNumber`: Current level (1-10+)
- `dropSpeedMs`: Automatic drop speed in milliseconds (400ms → 60ms)
- `scoreMultiplier`: Score multiplier for line clears (1.0x → 2.5x)

**Design**: Immutable class ensuring thread safety and predictable behavior.

#### 8.2.2 LinesClearedTracker
**File**: `com.comp2042.game.level.LinesClearedTracker.java`

**Purpose**: Tracks total lines cleared during the game

**Features**:
- Uses JavaFX `IntegerProperty` for reactive updates
- `addLines(int lines)`: Increments total
- `reset()`: Resets to zero on new game
- `linesProperty()`: Exposes property for binding

**Design Pattern**: Observer Pattern (via JavaFX properties)

#### 8.2.3 LevelStrategy
**File**: `com.comp2042.game.level.LevelStrategy.java`

**Purpose**: Strategy interface for level configuration retrieval

**Design Pattern**: Strategy Pattern

**Benefits**: Allows different progression schemes (linear, exponential, custom) without modifying core logic.

#### 8.2.4 DefaultLevelStrategy
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

#### 8.2.5 LevelManager
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

### 8.3 Integration Points

#### 8.3.1 SimpleBoard
- Initializes `LinesClearedTracker` and `LevelManager`
- Resets both on `newGame()`
- Provides accessors for `LevelManager`

#### 8.3.2 DownMoveCommand
- Tracks lines cleared via `LinesClearedTracker`
- Updates level via `LevelManager.updateLevel()`
- Applies level-based score multiplier
- Triggers `GuiController.onLevelUp()` notification

#### 8.3.3 GameController
- Binds `LevelManager` to `GuiController` if board is `SimpleBoard`
- Enables level display in UI

#### 8.3.4 GuiController
- Displays current level in UI label
- Shows "LEVEL X!" notification on level up
- Updates game timer speed based on current level
- Binds level property to label for automatic updates

### 8.4 UI Changes
- Added `levelLabel` to `gameLayout.fxml`
- Level displayed as "Level: X" next to score
- Level-up notifications appear similar to score notifications

### 8.5 Design Benefits
- **Strategy Pattern**: Easy to swap level progression schemes
- **Observer Pattern**: Automatic UI updates via property binding
- **Single Responsibility**: Each component has one clear purpose
- **Dependency Inversion**: `LevelManager` depends on `LevelStrategy` interface
- **Encapsulation**: Level configuration encapsulated in immutable `Level` objects

### Commit Details
- **Commit**: `180a32f`
- **Date**: November 18, 2025
- **Message**: "Additional Features: Implement level system"
- **Files Changed**: Added Level.java, LevelManager.java, LevelStrategy.java, DefaultLevelStrategy.java, LinesClearedTracker.java; Modified SimpleBoard.java, DownMoveCommand.java, GameController.java, GuiController.java, gameLayout.fxml

---

## 9. New Features: Ghost Piece

### 9.1 Overview
Ghost piece feature shows players where their current piece will land, helping with placement decisions. The ghost appears as a semi-transparent preview (30% opacity) of the piece at its landing position, updating in real-time as the player moves or rotates the piece.

### 9.2 Components

#### 9.2.1 ViewData Enhancement
**File**: `com.comp2042.game.data.ViewData.java`

**Changes**:
- Added `ghostX` and `ghostY` fields to store ghost piece position
- Added `getGhostX()` and `getGhostY()` accessor methods
- Updated constructor to accept ghost position parameters

**Design**: Ghost position calculated in `SimpleBoard` and passed through `ViewData` to UI layer, maintaining separation of concerns.

#### 9.2.2 SimpleBoard Ghost Calculation
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

#### 9.2.3 ColorMapper Ghost Support
**File**: `com.comp2042.ui.ColorMapper.java`

**New Method**: `getGhostFillColor(int code)`

**Purpose**: Returns semi-transparent version of piece colors for ghost rendering

**Implementation**:
- Retrieves solid color via `ColorStrategyRegistry`
- Converts to `Color` object if applicable
- Creates new `Color` with same RGB but 30% opacity (`GHOST_OPACITY = 0.3`)
- Returns transparent color for empty cells (code 0)

**Design**: Centralized ghost color logic ensures consistent visual style across all piece types.

#### 9.2.4 GuiController Ghost Rendering
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

#### 9.2.5 FXML Layout Update
**File**: `src/main/resources/gameLayout.fxml`

**Changes**:
- Added `<GridPane fx:id="ghostPanel" vgap="1" hgap="1"/>` to scene graph
- Positioned before `brickPanel` so active piece renders on top

### 9.3 Visual Design
- **Opacity**: 30% (0.3) for clear visibility without obscuring board
- **Positioning**: Same X as current piece, Y at landing position
- **Styling**: Rounded corners matching active piece style
- **Colors**: Semi-transparent versions of piece colors
- **Update Frequency**: Real-time updates on every move/rotation

### 9.4 Benefits
- **Improved Gameplay**: Players can see landing position before dropping
- **Better Placement**: Helps with strategic piece positioning
- **Visual Feedback**: Clear indication of where piece will land
- **Non-Intrusive**: Semi-transparent design doesn't obstruct view
- **Real-Time Updates**: Ghost position updates immediately on movement

### 9.5 Design Patterns & Principles
- **Separation of Concerns**: Ghost calculation in `SimpleBoard`, rendering in `GuiController`
- **Single Responsibility**: Each component handles one aspect (calculation, color, rendering)
- **Reusability**: `ColorMapper.getGhostFillColor()` used consistently
- **Encapsulation**: Ghost position encapsulated in `ViewData`
- **Observer Pattern**: Ghost updates automatically via `refreshBrick()` calls

### Commit Details
- **Primary Implementation**: Implemented across multiple commits as part of Additional-Features branch
- **Test Addition**: `a0b3ee4` (Nov 20, 2025) - "test: add JUnit tests for ghost piece feature"
- **Files Modified**: ViewData.java, SimpleBoard.java, ColorMapper.java, GuiController.java, gameLayout.fxml
- **Test File**: GhostBlockTest.java (13 test methods)

---

## 10. New Features: High Score Tracking

### 10.1 Overview
High score tracking system that automatically saves and loads the player's best score across game sessions. The high score persists to disk and displays prominently in the UI, with special notifications when a new high score is achieved.

### 10.2 Components

#### 10.2.1 HighScoreManager
**File**: `com.comp2042.game.score.HighScoreManager.java`

**Purpose**: Manages high score persistence, tracking, and automatic updates

**Features**:
- **Automatic Detection**: Observes current score and automatically detects when beaten
- **Persistent Storage**: Saves high score to disk in user's home directory (`~/.tetris/highscore.dat`)
- **Observer Pattern**: Uses JavaFX `IntegerProperty` for reactive UI binding
- **New High Score Flag**: Tracks if current game achieved new high score
- **Automatic Loading**: Loads saved high score on game start

**Implementation Details**:
```java
public class HighScoreManager {
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private boolean isNewHighScore = false;
    
    public HighScoreManager(Score currentScore) {
        loadHighScore();
        
        // Listen for score changes to detect new high scores
        currentScore.scoreProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > highScore.get()) {
                highScore.set(newVal.intValue());
                if (!isNewHighScore) {
                    isNewHighScore = true;
                }
            }
        });
    }
    
    public void saveHighScore() {
        // Save to ~/.tetris/highscore.dat
        Path highScoreFile = Paths.get(System.getProperty("user.home"), 
                                       ".tetris", "highscore.dat");
        // ... write to file ...
    }
}
```

**Design**:
- **Observer Pattern**: Automatically tracks score changes via JavaFX properties
- **Single Responsibility**: Only handles high score tracking and persistence
- **File I/O**: Uses Java NIO for modern file operations
- **Error Handling**: Gracefully handles missing files or I/O errors

#### 10.2.2 File Storage Format
**Location**: `~/.tetris/highscore.dat` (user's home directory)

**Format**: Binary file using `DataOutputStream`/`DataInputStream`
- Single integer value (4 bytes)
- Simple, efficient storage
- Platform-independent

**Benefits**:
- **Persistence**: Survives application restarts
- **Privacy**: Stored in user's home directory
- **Lightweight**: Only 4 bytes per high score
- **Fast**: Binary format for quick read/write

### 10.3 Integration Points

#### 10.3.1 SimpleBoard Integration
- Creates `HighScoreManager` in constructor
- Passes current `Score` object for observation
- Provides `getHighScoreManager()` accessor
- Resets new high score flag on `newGame()`

```java
public class SimpleBoard implements Board {
    private final HighScoreManager highScoreManager;
    
    public SimpleBoard(int width, int height) {
        // ... other initialization ...
        score = new Score();
        highScoreManager = new HighScoreManager(score);
    }
    
    @Override
    public void newGame() {
        // ... other resets ...
        highScoreManager.resetNewHighScoreFlag();
    }
}
```

#### 10.3.2 GameController Integration
- Binds high score to UI via `GuiController.bindHighScore()`
- Automatic binding when board is `SimpleBoard`
- No additional logic needed (handled by observer pattern)

#### 10.3.3 GuiController Integration
**UI Elements**:
- `@FXML private Label highScoreLabel`: Displays "High Score: X"
- Positioned below level label in UI
- Updates automatically via property binding

**New High Score Notification**:
- Shows "NEW HIGH SCORE!" notification on game over
- Only displayed if high score was beaten
- Automatically saves high score to disk

```java
public void gameOver() {
    gameTimer.stop();
    
    // Check for new high score
    if (highScoreManager != null && highScoreManager.isNewHighScore()) {
        NotificationPanel notificationPanel = new NotificationPanel("NEW HIGH SCORE!");
        groupNotification.getChildren().add(notificationPanel);
        notificationPanel.showScore(groupNotification.getChildren());
        highScoreManager.onGameEnd(); // Save high score
    }
    
    gameOverPanel.setVisible(true);
    isGameOver.setValue(Boolean.TRUE);
}
```

### 10.4 UI Changes

#### High Score Display
**FXML Layout** (`gameLayout.fxml`):
- Added `<Label fx:id="highScoreLabel" text="High Score: 0" layoutX="200" layoutY="60" styleClass="score-label"/>`
- Positioned below level display (Y=60)
- Uses same style class as score and level labels

**Visual Layout**:
```
Score: 1234
Level: 3
High Score: 5678
```

#### Game Over Panel Enhancement
**File**: `GameOverPanel.java`

**Changes**:
- Added "New Game (N)" button to game over panel
- Button allows immediate restart without closing panel
- Styled with custom CSS (`game-button` class)
- Verifies high score persistence across sessions

**Design**: Provides user-friendly way to test high score saving functionality

#### Pause Panel Enhancement
**File**: `PausePanel.java` (NEW)

**Purpose**: Dedicated pause panel with interactive controls

**Features**:
- Displays "PAUSED" message
- "Resume (P)" button - resumes game
- "New Game (N)" button - starts new game from pause
- Styled buttons with hover/pressed effects

**Integration**:
- Replaces simple text notification with interactive panel
- Button actions bound in `GuiController.showPauseNotification()`
- Allows starting new game without resuming current game

**CSS Styling** (`window_style.css`):
```css
.game-button {
    -fx-font-family: "Let's go Digital";
    -fx-font-size: 18px;
    -fx-background-color: linear-gradient(...);
    -fx-text-fill: white;
    -fx-padding: 10px 20px;
    -fx-background-radius: 5px;
    -fx-cursor: hand;
}
```

**Benefits**:
- **Better UX**: Clear, clickable buttons instead of text-only instructions
- **Testing Support**: Easy to verify high score persistence by starting new games
- **Visual Feedback**: Hover and pressed states provide interactivity
- **Consistency**: Both game over and pause panels have similar button styles

### 10.5 User Experience

#### First Time Playing
1. Game starts with High Score: 0
2. Player plays and achieves any score
3. On game over, score becomes new high score
4. High score saved automatically to disk

#### Subsequent Games
1. Game loads previous high score from disk
2. High score displays throughout gameplay
3. If player beats high score:
   - High score updates in real-time during play
   - "NEW HIGH SCORE!" notification shows on game over
   - New high score saved to disk
4. If player doesn't beat high score:
   - High score remains unchanged
   - No special notification
   - File not modified

### 10.6 Design Benefits
- **Observer Pattern**: Automatic high score detection via property listeners
- **Single Responsibility**: `HighScoreManager` only handles high score logic
- **Persistence**: Score survives application restarts
- **User Feedback**: Clear visual indication of high score achievement
- **Error Resilience**: Gracefully handles missing files or I/O errors
- **Platform Independent**: Works on Windows, macOS, Linux
- **Encapsulation**: File I/O details hidden from other components
- **Reactive Updates**: UI automatically updates via property binding

### 10.7 Technical Details

**File System Operations**:
- Directory creation: `Files.createDirectories()` if `.tetris` doesn't exist
- File writing: `DataOutputStream` for binary format
- File reading: `DataInputStream` with existence check
- Error handling: Try-catch with fallback to default value (0)

**Concurrency Considerations**:
- JavaFX properties are thread-safe for UI binding
- File I/O occurs on JavaFX Application Thread
- No explicit synchronization needed for single-player game

**Memory Footprint**:
- `HighScoreManager`: ~24 bytes (IntegerProperty + boolean)
- File on disk: 4 bytes
- Minimal memory impact

---

## 11. Object-Oriented Concepts Demonstrated

### 10.1 Encapsulation
- Private fields with controlled access via getters
- Immutable DTOs (ViewData, Level)
- Internal state hidden from clients

### 10.2 Inheritance
- Brick classes extend/implement `Brick` interface
- Color strategies implement `ColorStrategy` interface
- Commands implement `MoveCommand` interface

### 10.3 Polymorphism
- Strategy pattern: Different strategies used polymorphically
- Command pattern: Commands executed polymorphically
- Interface segregation: Different board implementations

### 10.4 Abstraction
- Interfaces hide implementation details
- Abstract concepts (Board, Brick, Strategy) separate from concrete implementations

### 10.5 Composition
- `SimpleBoard` composes `BrickGenerator`, `BrickRotator`, `Score`, `LevelManager`
- `LevelManager` composes `LinesClearedTracker` and `LevelStrategy`
- Components work together without tight coupling

---

## 12. SOLID Principles Applied

### 12.1 Single Responsibility Principle (SRP)
- Each class has one reason to change
- Examples: `InputHandler` (input only), `GameTimer` (timing only), `GameViewModel` (rendering only)

### 12.2 Open/Closed Principle (OCP)
- Open for extension, closed for modification
- Examples: New colors via `ColorStrategy`, new commands via `MoveCommand`, new level strategies via `LevelStrategy`

### 12.3 Liskov Substitution Principle (LSP)
- Subtypes must be substitutable for their base types
- Examples: Any `Brick` implementation works, any `LevelStrategy` works, any `ColorStrategy` works

### 12.4 Interface Segregation Principle (ISP)
- Clients shouldn't depend on interfaces they don't use
- Examples: Segregated `Board` interfaces (`MovableBoard`, `ClearableBoard`, etc.)

### 12.5 Dependency Inversion Principle (DIP)
- Depend on abstractions, not concretions
- Examples: `GameController` depends on `Board` interface, `SimpleBoard` depends on `BrickGenerator` interface, `LevelManager` depends on `LevelStrategy` interface

---

## 13. Build & Run Instructions

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

## 14. Git History

### Branch Strategy
- `master`: Main development branch with all stable features
- `Additional-Features`: Feature branch for new gameplay features (level system, ghost piece, hard drop)
- `refactor/*` branches: Short-lived branches for specific refactoring work
  - `refactor/packaging-and-organisation`
  - `refactor/basic-maintenance`
  - `refactor/single-responsibility`
  - `refactor/remove-unused-code`

### Chronological Development Timeline

#### Phase 1: Initial Setup (September-October 2025)
- **Sep 29** (`dc55722`): Initial commit - base Tetris game

#### Phase 2: Package Reorganization (October 2025)
- **Oct 28** (`732b2d7`): Package reorganization into `app/`, `game/`, `ui/` structure
  - 31 files reorganized
  - All imports updated
  - `pom.xml` and FXML references fixed

#### Phase 3: Basic Maintenance (October 2025)
- **Oct 28** (`6b4f6c0`): Basic maintenance and bug fixes
  - Fixed score binding
  - Renamed `createNewBrick()` → `trySpawnNewBrick()`
  - Added score display to UI
  - 7 files modified

#### Phase 4: Single Responsibility Refactoring (October-November 2025)
- **Oct 30** (`dec9e11`): Extracted UI components (SRP)
  - Created `GameTimer`, `InputHandler`, `GameViewModel`, `ColorMapper`
  - Reduced `GuiController` from 200+ lines to focused responsibilities
  - 5 new files, GuiController simplified

- **Nov 2** (`c577acc`): Dependency Injection and Factory Pattern
  - Implemented `BrickGeneratorFactory`
  - GameController now accepts Board interface
  - 13 files modified

#### Phase 5: Testing Infrastructure (November 2025)
- **Nov 12** (`c0e0ebc`): Initial JUnit 5 test suite
  - Added tests for bricks, operations, score
  - Configured Maven Surefire for JUnit Platform
  - 9 test files created

#### Phase 6: Javadoc Documentation (November 2025)
- **Nov 17** (`e85c90c`): Comprehensive Javadoc coverage
  - Documented all design patterns and SOLID principles
  - Added @param and @return tags to all public methods
  - Converted inline comments to proper Javadoc format
  - 30 files documented

#### Phase 7: New Features - Level System (November 2025)
- **Nov 18** (`180a32f`): Level system implementation
  - Added 5 new level-related classes
  - Adaptive difficulty every 5 lines
  - Speed scaling (400ms → 60ms)
  - Score multipliers (1.0x → 2.5x)

#### Phase 8: New Features - Input Latency Fix (November 2025)
- **Nov 19** (`ce55a4d`): Fixed input latency
  - Modified `InputHandler` with callback mechanism
  - Immediate UI refresh on key press
  - Critical bug fix for gameplay feel

#### Phase 9: New Features - Hard Drop & Testing (November 2025)
- **Nov 20** (`8b628d3`): Hard drop feature
  - Added `HardDropMoveCommand`
  - Space bar control, 2 points per cell
  
- **Nov 20** (`a0b3ee4`): Ghost piece tests
  - Added `GhostBlockTest.java` with 13 tests

- **Nov 20** (`12cb666`): Mockito dependencies
  - Added Mockito for mocking in tests

- **Nov 20** (`6593775`): Comprehensive test suite
  - Added tests for all game components
  - Total: 31 test files, 189+ test methods

#### Phase 10: New Features - Pause Functionality (November 2024)
- **Nov 24** (`5b0b55b`): Reflection effect on game over panel
- **Nov 24** (`e55a350`): Pause/resume with P key
- **Nov 24** (`6804065`): Merged Additional-Features into refactor/basic-maintenance

#### Phase 11: Final Refinements (November 2025)
- **Nov 25** (`8ccda11`): Remove unused code and clean imports
- **Nov 25** (`7b045cb`): Merged refactor/remove-unused-code cleanup
- **Nov 25** (`1acd126`): Fixed brick spawn position and pause notification UI
- **Nov 25** (`4141056`): Extract constants, reduce duplication, improve naming
- **Nov 25** (`0419cd8`): Comprehensive tests for data package and HardDropMoveCommand

### Key Statistics
- **Total Commits**: 18 commits
- **Duration**: September 29 - November 25, 2025 (~2 months)
- **Files Created**: 60+ Java files (31 test files + 30+ source files)
- **Lines of Code**: Significant codebase growth with proper organization
- **Test Coverage**: 189+ test methods across 31 test files

---

## 15. Technical Implementation Details

### 15.1 Performance Optimizations
- **O(1) Color Strategy Lookup**: `ColorStrategyRegistry` uses HashMap for constant-time color retrieval
- **Ghost Piece Calculation**: Efficient simulation using existing collision detection (`MatrixOperations.intersect`)
- **Property Binding**: JavaFX properties enable reactive UI updates without manual refresh loops
- **Command Pattern Caching**: `DownMoveCommand` caches result in `DownData` for efficient retrieval

### 15.2 Code Quality Metrics
- **Reduced Coupling**: GameController no longer creates its own dependencies
- **Increased Cohesion**: Each class has single, well-defined responsibility
- **Eliminated Code Smells**:
  - Removed large switch statements (replaced with Strategy pattern)
  - Removed nested conditionals (replaced with Command pattern)
  - Removed duplicate code through extraction
  - Removed unused imports and dead code
- **Improved Naming**: Clear, intention-revealing names (`trySpawnNewBrick` vs `createNewBrick`)

### 15.3 Design Pattern Interactions
The patterns work together synergistically:

1. **Factory + Dependency Injection**:
   - `BrickGeneratorFactory` creates generators
   - Generators injected into `SimpleBoard` constructor
   - Result: Testable, flexible brick generation

2. **Command + Observer**:
   - Commands modify game state (score, lines cleared)
   - JavaFX properties observe changes
   - UI automatically updates
   - Result: Reactive, decoupled UI

3. **Strategy + Registry**:
   - Multiple color strategies implement interface
   - Registry manages strategy lookup
   - Polymorphic color selection
   - Result: Extensible color system

4. **Interface Segregation + Dependency Injection**:
   - Board split into focused interfaces
   - Commands depend only on needed interfaces
   - Easy to mock for testing
   - Result: Testable, loosely coupled commands

### 15.4 JavaFX Integration
- **FXML Controllers**: `GuiController` linked via `fx:controller` attribute
- **Property Binding**: Score and Level use `IntegerProperty` for automatic UI sync
- **Timeline Animation**: `GameTimer` uses JavaFX `Timeline` for game loop
- **Reflection Effect**: Game over panel uses JavaFX `Reflection` effect for visual polish
- **CSS Styling**: `window_style.css` provides custom styling for labels and panels

### 15.5 Testing Strategy
- **Unit Tests**: Test individual components in isolation (MatrixOperations, Score, Bricks)
- **Integration Tests**: Test component interactions (GameController, Commands)
- **Mocking**: Mockito used to mock dependencies (Board, GuiController)
- **Test Data**: Predefined brick shapes and board states for consistent testing
- **Edge Cases**: Test boundary conditions (empty board, full board, collisions)

---

## 16. Future Enhancements

### Potential Features
- **Next Piece Preview**: Display upcoming piece (data already available in ViewData)
- **Hold Piece**: Allow players to hold/swap current piece
- **Combo System**: Bonus points for consecutive line clears
- **Sound Effects**: Audio feedback for actions
- **Leaderboard**: Track top 10 scores with names

### Code Improvements
- Additional unit tests for new features
- Integration tests for game flow
- Performance optimizations
- Additional design pattern applications

---

## 17. Lessons Learned & Best Practices

### 17.1 Design Pattern Application
- **When to use Strategy**: Replace switch statements or if-else chains with polymorphic strategies
- **When to use Command**: Encapsulate operations as objects for undo/redo, queuing, or logging
- **When to use Factory**: Decouple object creation from usage, especially when creation logic is complex
- **When to use Observer**: Enable reactive updates without tight coupling between components
- **When to use Registry**: Centralize strategy/object lookup for easier management

### 17.2 SOLID Principles in Practice
- **SRP**: Ask "does this class have more than one reason to change?" If yes, split it
- **OCP**: Design for extension (new strategies, commands) without modification (existing code)
- **LSP**: Ensure subtypes can replace base types without breaking behavior
- **ISP**: Don't force clients to depend on methods they don't use - create focused interfaces
- **DIP**: Depend on abstractions (interfaces) not concretions (classes)

### 17.3 Refactoring Approach
1. **Start with organization**: Clean package structure makes everything else easier
2. **Fix bugs early**: Address known issues before major refactoring
3. **Extract incrementally**: Don't try to refactor everything at once
4. **Test after each change**: Ensure functionality preserved after each refactoring step
5. **Document as you go**: Javadoc and README updated alongside code changes

### 17.4 Testing Insights
- **Test early**: Setting up test infrastructure early makes TDD easier
- **Mock dependencies**: Mockito essential for isolating units under test
- **Test behavior, not implementation**: Focus on what code does, not how it does it
- **Edge cases matter**: Boundary conditions often reveal bugs

### 17.5 JavaFX Best Practices
- **Separate concerns**: UI logic in controller, business logic in model
- **Use property binding**: Automatic synchronization better than manual updates
- **FXML for layouts**: Declarative UI description cleaner than programmatic creation
- **Event handlers extracted**: `InputHandler` separation improves testability

---

## 18. Summary

This project demonstrates:
- ✅ Meaningful package organization
- ✅ Basic maintenance (renaming, encapsulation, cleanup)
- ✅ Single Responsibility Principle through class decomposition
- ✅ Multiple design patterns (Strategy, Factory, Command, Observer)
- ✅ Comprehensive JUnit test suite
- ✅ Bug fixes improving gameplay experience
- ✅ New gameplay features:
  - **Hard Drop**: Instant piece drop with Space key (Command Pattern)
  - **Pause/Resume**: Game pause functionality with P key
  - **Level System**: Adaptive difficulty with Strategy pattern (every 5 lines = new level)
  - **Ghost Piece**: Semi-transparent landing preview with real-time updates
  - **High Score Tracking**: Persistent high score with automatic save/load (Observer Pattern)
- ✅ SOLID principles throughout
- ✅ Object-oriented concepts (Encapsulation, Inheritance, Polymorphism, Abstraction, Composition)

All changes maintain backward compatibility while significantly improving code quality, maintainability, and gameplay experience.

