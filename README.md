# COMP2042 Tetris Refactoring & Enhancement Project

## GitHub Repository

**Repository URL**: https://github.com/Aariz27/CW2025

---

## Compilation Instructions

### Prerequisites
- **Java Development Kit (JDK)**: Version 23
- **Maven**: Maven 3.6+ (or use the included Maven wrapper)
- **JavaFX**: Version 21.0.6 (automatically downloaded by Maven)

### Step-by-Step Compilation

1. **Clone the repository**:
```bash
git clone [https://github.com/Aariz27/CW2025]
cd CW2025
```

2. **Clean and compile the project**:
```bash
./mvnw clean compile
```
   - On Windows, use `mvnw.cmd` instead of `./mvnw`
   - This will download all dependencies automatically
   - JavaFX libraries will be fetched from Maven Central

3. **Run the application**:
```bash
./mvnw clean javafx:run
```
   - The main menu will launch automatically
   - Choose "Start Game" to begin playing Tetris
   - Choose "Controls" to view all game controls
   - No additional configuration needed

4. **Run tests** (optional):
```bash
./mvnw test
```
   - Executes all JUnit test cases
   - Test results will be displayed in the console

5. **Generate Javadoc** (optional):
```bash
./mvnw javadoc:javadoc
```
   - Documentation generated in `target/site/apidocs/`

### Dependencies
All dependencies are managed via Maven and automatically downloaded:
- **JavaFX Controls & FXML**: Version 21.0.6
- **JUnit Jupiter**: Version 5.12.1 (testing framework)
- **Mockito**: Version 5.14.2 (mocking framework for tests)

### Troubleshooting
- If compilation fails, ensure Java 23 is installed: `java -version`
- If JavaFX modules are not found, ensure Maven can download dependencies (check internet connection)
- On macOS, you may need to allow the application in Security & Privacy settings

---

## Implemented and Working Properly

### 1. Main Menu System
**Description**: A professional main menu system that appears on application startup, replacing the immediate game launch.

**Features**:
- **Main Menu Screen**: Displays "TETRIS JavaFX Edition" title with two options
- **Start Game**: Launches the actual Tetris gameplay with all features enabled
- **Controls Screen**: Comprehensive reference of all game controls with clear explanations
- **Scene Management**: Smooth transitions between menu, controls, and game screens
- **Lazy Loading**: Game components initialize only when "Start Game" is selected
- **Consistent Styling**: Menu and controls screens match the pause UI aesthetic

**Navigation Flow**:
- **App Launch** → Main Menu appears
- **"Controls"** → View control reference → **"Back to Menu"** → Main Menu
- **"Start Game"** → Full Tetris gameplay begins

**Implementation**:
- **MainMenuPanel**: Custom panel with title and navigation buttons
- **ControlsPanel**: Detailed control reference with categorized explanations
- **Scene Management**: Multiple JavaFX scenes with proper transitions
- **Performance**: Game loads on-demand for faster startup

**Design**: Provides a polished gaming experience with clear navigation and educational value through the controls reference.

---

### 2. Package Reorganization (Refactoring)
**Description**: Restructured the entire codebase from a flat package structure into a well-organized, feature-focused hierarchy.

**Structure**:
- `com.comp2042.app/` - Application entry point (Main.java)
- `com.comp2042.ui/` - User interface components (12 classes + color package)
- `com.comp2042.game.board/` - Board management (6 classes)
- `com.comp2042.game.bricks/` - Tetromino pieces (10 classes)
- `com.comp2042.game.controller/` - Game logic (1 class + 6 command classes)
- `com.comp2042.game.data/` - Data transfer objects (4 classes)
- `com.comp2042.game.events/` - Event handling (4 classes)
- `com.comp2042.game.operations/` - Utility operations (2 classes)
- `com.comp2042.game.score/` - Scoring system (2 classes)
- `com.comp2042.game.level/` - Level progression (5 classes)

**Benefits**: The new structure makes it much easier to navigate the codebase and find related classes. It also improves maintainability and makes it easier to add new features in the future.

---

### 2. Single Responsibility Principle (SRP) - Class Decomposition
**Description**: Decomposed `GuiController` (which had 200+ lines with multiple responsibilities) into focused classes.

**Extracted Classes**:
- **InputHandler**: Keyboard input processing (arrow keys, WASD, Space, P, N, C, T)
- **GameTimer**: Game loop timing and automatic piece dropping
- **GameViewModel**: Data-to-UI transformations and rendering calculations
- **ColorMapper**: Color code to Paint object mapping
- **NotificationPanel**: Score and notification display
- **PausePanel**: Pause screen with interactive buttons
- **GameOverPanel**: Game over screen with restart option
- **MainMenuPanel**: Main menu with title and navigation options
- **ControlsPanel**: Comprehensive game controls reference screen

**Result**: Each class now focuses on doing one thing well, which makes the code easier to test and maintain. The codebase now has 12 focused UI classes instead of the original monolithic GuiController.

---

### 3. Design Patterns Implementation

#### 3.1 Strategy Pattern
**Color Strategy** (`com.comp2042.ui.color`):
- Replaced 10-case switch statement with polymorphic strategy selection
- 9 concrete strategies: `AquaColorStrategy`, `BlueVioletColorStrategy`, `DarkGreenColorStrategy`, `YellowColorStrategy`, `RedColorStrategy`, `BeigeColorStrategy`, `BurlyWoodColorStrategy`, `TransparentColorStrategy`, `WhiteColorStrategy`
- `ColorStrategyRegistry` uses HashMap for efficient strategy lookup
- **Benefit**: New colors can be added by creating new strategy classes without changing existing code

**Level Strategy** (`com.comp2042.game.level`):
- `LevelStrategy` interface with `DefaultLevelStrategy` implementation
- Allows different level progression schemes to be swapped
- **Benefit**: Makes it easy to experiment with different difficulty progression schemes

**Theme Strategy** (`com.comp2042.ui.theme`):
- `Theme` interface with multiple concrete implementations: `ClassicTheme`, `RetroTheme`, `NeonTheme`
- Each theme provides its own stylesheet, color palette, and name
- Themes can be swapped at runtime without affecting game logic
- **Benefit**: Allows complete visual redesigns without changing game code, demonstrates Open/Closed Principle

#### 3.2 Singleton Pattern
**ThemeManager** (`com.comp2042.ui.theme`):
- Manages globally accessible current theme state
- Single instance ensures consistent theme across entire application
- Provides centralized point for theme switching
- **Benefit**: Guarantees single source of truth for current theme, easy global access without passing references

#### 3.3 Factory Pattern
**BrickGeneratorFactory** (`com.comp2042.game.bricks`):
- Decouples `SimpleBoard` from concrete `RandomBrickGenerator`
- Factory creates generators via `createDefault()` method
- **Benefit**: Makes it easy to swap in different brick generators for testing or implementing different randomization systems

#### 3.4 Command Pattern
**MoveCommand Interface** (`com.comp2042.game.controller.commands`):
- Encapsulates move operations as objects
- Concrete commands: `LeftMoveCommand`, `RightMoveCommand`, `RotateMoveCommand`, `DownMoveCommand`, `HardDropMoveCommand`
- **Benefit**: Eliminates nested if-else blocks, makes each move type independently testable, and makes it easy to add new types of moves

#### 3.5 Observer Pattern
**JavaFX Properties**:
- `Score.scoreProperty()` bound to UI label
- `LevelManager.levelProperty()` bound to UI label
- `LinesClearedTracker.linesProperty()` observed by `LevelManager`
- **Benefit**: The UI updates automatically without needing to manually call refresh methods

#### 3.6 Registry Pattern
**ColorStrategyRegistry**:
- Centralized management of color strategies
- HashMap-based lookup for efficient strategy retrieval
- **Benefit**: Cleaner than if-else chains, easy to extend

---

### 4. Interface Segregation Principle (ISP)
**Description**: Segregated the monolithic `Board` interface into focused sub-interfaces.

**Segregated Interfaces**:
- `MovableBoard` - Movement operations (move left/right/down, rotate)
- `ClearableBoard` - Row clearing operations (merge, clear rows)
- `SpawnableBoard` - Brick spawning operations (spawn, new game)
- `ScorableBoard` - Score access

**Main Interface**: `Board` extends all segregated interfaces for complete functionality.

**Benefit**: Different parts of the code only need to depend on the specific interface methods they actually use (e.g., `InputHandler` only needs the movement methods).

---

### 5. Dependency Injection
**Description**: Reduced coupling by injecting dependencies via constructors.

**Examples**:
- **GameController**: Accepts `Board` interface (not concrete `SimpleBoard`)
- **SimpleBoard**: Uses `BrickGeneratorFactory` for generator creation
- **LevelManager**: Accepts `LinesClearedTracker` and `LevelStrategy` via constructor

**Benefit**: Makes testing easier by allowing mock objects to be injected, and reduces coupling between classes.

---

### 6. Bug Fixes

#### 6.1 Input Latency Fix
**Problem**: Significant delay between key press and piece movement.

**Solution**: Added callback parameter to `InputHandler`. Now captures `ViewData` from events and immediately calls `GuiController.refreshBrick()`.

**Result**: The UI now updates instantly when keys are pressed, eliminating the lag.

#### 6.2 Score Display Fix
**Problem**: Score was never displayed despite `Score` class exposing `IntegerProperty`.

**Solution**: Added `scoreLabel` to FXML and implemented `bindScore()` in `GuiController`.

**Result**: The score now displays properly and updates in real-time.

#### 6.3 Method Naming Clarification
**Problem**: `createNewBrick()` returned `true` on collision (failure), which was counterintuitive.

**Solution**: Renamed to `trySpawnNewBrick()` to clearly indicate it's an attempt that may fail.

**Result**: The code is now clearer about what it's trying to do.

---

### 7. New Gameplay Features

#### 7.6 Time Manipulation & Undo (G, U Keys)
**Description**: Earnable power-ups tied to cleared lines that let you briefly slow time or undo your last move.

**Features**:
- **Slow Time (G key)**: Every 3 cleared lines grants a charge; pressing **G** slows automatic drop speed by +0.5s for 5 seconds, then reverts.
- **Undo Last Move (U key)**: Every 5 cleared lines grants an undo; pressing **U** restores the previous board state (board, active/held pieces, queue, score, level, lines).
- **Safety Checks**: Abilities are disabled while paused or after game over; charges are only consumed when available.
- **Feedback**: On-screen notifications for activation; timers reset on new game and halt on game over.

**Implementation**:
- Snapshot-driven restore (`BoardStateSnapshot`, `UndoData`) captured before each user move.
- Ability gating/consumption in `GameController`; timer adjustment in `GuiController`.
- Input wiring and controls UI updated for G/U keys.

#### 7.1 Hard Drop (Space Bar)
**Description**: Instantly drops the current piece to its landing position (ghost position).

**Features**:
- Piece moves to landing position immediately
- Awards **2 points per cell dropped** as bonus
- Triggers normal landing logic (merge, clear rows, spawn new piece)
- Applies level-based score multipliers
- Shows score notifications for cleared lines

**Implementation**: `HardDropMoveCommand` using Command Pattern.

**Controls**: Press **SPACE** key.

#### 7.2 Pause/Resume (P Key)
**Description**: Allows players to pause and resume the game mid-play.

**Features**:
- Pauses game timer (stops automatic piece dropping)
- Shows "PAUSED" overlay with interactive buttons
- Prevents all movement input during pause
- Resumes with current level speed on unpause
- Cannot pause when game is over

**Implementation**: `GuiController.togglePause()` with `BooleanProperty` for state management.

**Controls**: Press **P** key to toggle pause.

#### 7.3 Level System
**Description**: Comprehensive level progression system with adaptive difficulty.

**Features**:
- **Level Progression**: Advance 1 level every 5 lines cleared
- **Drop Speed Scaling**: Progressively faster piece drop from Level 1 to Level 10+
- **Score Multipliers**: Increasing score multipliers from 1.0x to 2.5x as levels progress
- **Visual Feedback**: "LEVEL X!" notification on level up
- **UI Display**: Current level shown in game interface

**Components**:
- `Level`: Immutable configuration object (level number, drop speed, multiplier)
- `LinesClearedTracker`: Tracks total lines cleared (Observable)
- `LevelStrategy`: Strategy interface for level configuration
- `DefaultLevelStrategy`: Provides predefined level configurations
- `LevelManager`: Manages level progression based on lines cleared

**Design**: Uses Strategy Pattern for flexible progression schemes and Observer Pattern for automatic UI updates.

#### 7.4 Ghost Piece
**Description**: Semi-transparent preview showing where the current piece will land.

**Features**:
- **Real-Time Updates**: Ghost position updates on every move/rotation
- **Visual Design**: Semi-transparent appearance for clear visibility without obscuring board
- **Positioning**: Same X as current piece, Y at calculated landing position
- **Styling**: Rounded corners matching active piece style

**Implementation**:
- `ViewData` enhanced with `ghostX` and `ghostY` fields
- `SimpleBoard.findDropY()` calculates landing position via collision simulation
- `ColorMapper.getGhostFillColor()` provides semi-transparent colors
- `GuiController` renders ghost via dedicated `ghostPanel` GridPane

**Benefit**: Helps players with strategic piece placement decisions.

#### 7.5 High Score Tracking
**Description**: Automatic high score tracking with persistent storage.

**Features**:
- Automatically tracks when the current score beats the high score
- Saves high scores to a file (`~/.tetris/highscore.dat`) so they persist between game sessions
- Shows the high score in the UI throughout gameplay
- Displays a "NEW HIGH SCORE!" notification when you beat your previous best
- High scores are preserved even after closing and reopening the game

**Implementation**:
- `HighScoreManager`: Manages tracking, detection, and file I/O
- Uses JavaFX `IntegerProperty` for reactive UI binding
- Binary file format (DataOutputStream/DataInputStream) for efficient storage

**Design**: Uses Observer Pattern for automatic detection and file I/O for persistence.

---

### 8. Next Piece Preview
**Description**: Real-time preview of the upcoming tetromino piece, allowing players to plan their next moves strategically.

**Features**:
- **Live Preview**: Shows the actual shape of the next piece using full brick size (24px)
- **Dynamic Positioning**: Automatically adapts to different brick shapes and dimensions
- **Theme Integration**: Preview colors update dynamically when switching themes (Classic, Retro, Neon)
- **Clean Layout**: Positioned in right sidebar below score/level/high score labels
- **Backend Ready**: Utilizes existing `ViewData.getNextBrickData()` and `RandomBrickGenerator` queue

**Implementation**:
- `GuiController` manages preview rendering with `nextBrickRectangles` array and `nextBrickPanel`
- `initNextBrickPreview()` initializes preview panel on game start
- `updateNextBrickPreview()` refreshes preview when new pieces spawn
- Integrated into `refreshBrick()` for synchronized updates with active piece
- Theme changes trigger preview color updates

**Technical Details**:
- `nextBrickContainer` Pane (120×120px) holds preview area
- `nextBrickPanel` GridPane renders individual brick rectangles
- Preview uses same visual style as active pieces for consistency
- "Next Piece" label positioned above preview area

**Design**: Provides strategic gameplay advantage without cluttering the interface. Players can plan sequences and avoid surprise pieces.

---

### 10. Hold Piece Mechanics
**Description**: A strategic gameplay feature allowing players to store and swap the current falling piece with a held piece, adding tactical depth to piece placement decisions.

**Features**:
- **Hold Storage**: Players can store the current piece by pressing **C** key
- **Piece Swapping**: If a piece is already held, pressing **C** swaps the current piece with the held piece
- **Visual Display**: Held piece shown in a dedicated panel in the right sidebar above the "Next Piece" preview
- **One-Time Use**: Each held piece can only be swapped once per spawn cycle
- **Game State Preservation**: Hold state resets when starting a new game
- **Theme Integration**: Hold piece preview colors update dynamically when switching themes

**Implementation**:
- **Backend Logic**: `SimpleBoard.holdBrick()` manages piece storage and swapping logic
- **UI Integration**: `GuiController` renders held piece in dedicated `holdBrickPanel` with `holdBrickRectangles` array
- **Input Handling**: **C** key mapped to `InputHandler.onHoldRequested` callback
- **Data Transfer**: `ViewData` enhanced with `holdBrickData` field for rendering
- **Event System**: New `HOLD` event type added to `EventType` enum and `InputEventListener` interface

**Technical Details**:
- `holdBrickContainer` Pane (120×120px) provides visual container for held piece
- `holdBrickPanel` GridPane renders individual brick rectangles with proper positioning
- "Hold (C)" label indicates the control key and purpose
- Piece storage persists until swapped or new game starts
- Supports all brick shapes with automatic layout adaptation

**Design**: Adds strategic depth by allowing players to save useful pieces for later while managing immediate placement challenges. Particularly valuable for advanced Tetris players who want to optimize piece sequences.

---

### 11. Visual Themes System
**Description**: A robust theming system using Strategy and Singleton patterns that allows players to switch between distinct visual styles during gameplay without affecting game mechanics or state.

**Features**:
- **Runtime Theme Switching**: Players can instantly toggle between themes by pressing the **T** key during gameplay
- **State Preservation**: Game state (board, score, active piece, level) is perfectly preserved during theme switches
- **Multiple Themes**: Three fully implemented themes with distinct visual styles

**Available Themes**:

#### Classic Theme (Default)
- Original game visual style
- Standard color palette with colored bricks
- Traditional UI styling

#### Retro Theme
- Monochrome green aesthetic inspired by classic handheld consoles (Gameboy-style)
- All game elements rendered in various shades of green
- Nostalgic retro gaming experience

#### Neon Theme
- High-contrast colors with glow effects for bricks
- Dark background for dramatic appearance
- Modern, vibrant aesthetic

**Implementation**:
- **Strategy Pattern**: `Theme` interface defines contract for visual themes (stylesheets, color palettes, names)
- **Singleton Pattern**: `ThemeManager` manages active theme and notifies UI of changes
- **Observer Pattern**: Theme changes trigger automatic UI updates
- **Dynamic CSS**: Stylesheets swapped at runtime without application restart
- **Color Delegation**: `ColorMapper` delegates to current theme for brick colors

**Technical Details**:
- `Theme` interface: Defines `getStylesheet()`, `getColorPalette()`, `getThemeName()`
- `ThemeManager`: Singleton managing current theme with change notification
- Concrete implementations: `ClassicTheme`, `RetroTheme`, `NeonTheme`
- `GuiController.initThemeHandling()`: Listens for theme changes, swaps CSS, triggers repaint
- `ColorMapper.getFillColor()`: Delegates to active theme instead of static registry
- `InputHandler`: Captures 'T' key for theme toggle

**Design**: Demonstrates Strategy pattern for visual variation, Singleton for global theme state, and clean separation between game logic and presentation.

---

### 10. Visual Feedback Effects
**Description**: Dynamic visual effects that provide immediate feedback when lines are cleared, enhancing the game's visual polish without affecting gameplay mechanics.

**Features**:

#### Row Clear Shockwave
- Whenever lines are cleared (via normal fall or hard drop), the board border briefly flashes with a bright shockwave effect
- Implementation: `GuiController` adds a `shockwave` CSS class to the game board and removes it after approximately 225ms
- CSS applies a strong white drop shadow around the board border for the flash effect

#### Brick Border Bounce
- On every row clear, all visible bricks (board tiles and active piece) get a subtle colored border pulse animation
- Each brick's stroke animates from 0px → 2px → 0px in its own fill color over approximately 280ms
- Creates a quick "bounce" effect around each brick using JavaFX Timeline
- Provides satisfying visual feedback tied to the brick's actual color

**Gameplay Impact**:
- Purely visual feedback effects
- No changes to gameplay rules, scoring, or controls
- Triggered automatically on line clears from both normal drops and hard drops

**Implementation**:
- `triggerShockwaveEffect()` method toggles style class on game board
- `triggerBrickBounceEffect()` and `applyBounceToMatrix()` methods apply per-brick stroke animations
- Integrated into `moveDown()` and `handleHardDrop()` to detect line clears from `DownData`

**Design**: Provides immediate, satisfying visual feedback for successful line clears without cluttering the screen or distracting from gameplay.

---

### 11. Comprehensive JUnit Test Suite
**Description**: Growing test suite with 200+ test methods across 35+ test files covering all major components. Additional tests continue to be added as new features are implemented and refactored.

**Test Coverage**:
- **Brick Tests**: All 7 brick types (I, J, L, O, S, T, Z) with shape and color verification
- **Operations Tests**: MatrixOperations (collision, merge, row clearing)
- **Controller Tests**: GameController and all 5 command classes
- **Board Tests**: SimpleBoard operations and ghost piece calculation
- **Level System Tests**: Level progression, score multipliers, lines tracking
- **Data Tests**: All DTOs (ViewData, DownData, ClearRow, NextShapeInfo)
- **Event Tests**: Event creation, types, and event source
- **Score Tests**: Score increment, property binding, reset, high score tracking
- **High Score Manager Tests**: Score persistence, new high score detection, file I/O
- **Color System Tests**: ColorMapper (color codes 0-7, ghost colors, unknown codes), ColorStrategyRegistry (strategy lookup, fallback behavior), all ColorStrategy implementations
- **UI Component Tests**: Most UI components tested (PausePanel intentionally skipped due to JavaFX toolkit initialization requirements)

**Frameworks**: JUnit 5 (Jupiter), Mockito for mocking

**Run Command**: `./mvnw test`

**Note on Test Execution**: Some tests may encounter errors when running on Java 25. The project is configured for Java 23 (see `pom.xml`), but the runtime JVM that actually executes Maven and the tests is Java 25 (installed on the system). This can cause compatibility issues with Mockito's ByteBuddy dependency, which hasn't been fully updated for Java 25 yet. The ByteBuddy experimental flag in the Maven Surefire configuration helps mitigate most issues, but occasional test failures may occur due to this version mismatch.

---

### 12. SOLID Principles Applied Throughout

- **Single Responsibility Principle**: Each class has one reason to change (e.g., `InputHandler`, `GameTimer`, `GameViewModel`)
- **Open/Closed Principle**: Open for extension via strategies/commands, closed for modification
- **Liskov Substitution Principle**: All implementations substitutable for their interfaces
- **Interface Segregation Principle**: Segregated `Board` into focused interfaces
- **Dependency Inversion Principle**: Depend on abstractions (interfaces), not concretions (classes)

---

### 13. Complete Game Controls

| Key | Action | Description |
|-----|--------|-------------|
| **LEFT** / **A** | Move Left | Move piece one cell left |
| **RIGHT** / **D** | Move Right | Move piece one cell right |
| **UP** / **W** | Rotate | Rotate piece 90° counterclockwise |
| **DOWN** / **S** | Soft Drop | Move piece down one cell |
| **SPACE** | Hard Drop | Instantly drop piece to landing position (+2 pts/cell) |
| **G** | Slow Time | Spend a charge (every 3 rows) to slow drop speed for 5s |
| **U** | Undo Last Move | Spend a charge (every 5 rows) to undo your last move |
| **P** | Pause/Resume | Toggle pause state |
| **T** | Theme Toggle | Switch between visual themes (Classic/Retro/Neon) |
| **N** | New Game | Start a new game (anytime) |

---

## Implemented but Not Working Properly

### Neon Theme Background Issue
**Problem**: While the Neon Theme's high-contrast colors and glow effects for bricks are working correctly, the background is not appearing as "pitch black" as intended.

**Current Behavior**: The background retains the dark gray or original background image despite CSS attempts to override it with `-fx-background-image: none`.

**Investigation**: This appears to be related to CSS specificity issues or JavaFX scene property caching. The background color/image property may require a more aggressive reset or direct JavaFX API calls instead of pure CSS.

**Attempted Solutions**:
- Tried CSS override with `-fx-background-image: none`
- Attempted various CSS specificity approaches
- Current workaround: Theme works functionally but aesthetic is not optimal

**Status**: The theme is usable and functional for gameplay, but the visual aesthetic is not meeting design specifications. Further investigation into JavaFX scene graph property manipulation needed.

---

## Features Not Implemented

The following features were considered but not implemented due to time constraints and scope prioritization:


### 3. Combo System
**Reason**: A combo system rewarding consecutive line clears (e.g., Tetris → Tetris) would need:
- Tracking consecutive clears without placing non-clearing pieces
- Bonus score calculation logic
- Visual feedback for combo chains
- Balance testing to avoid score inflation

**Complexity**: Medium - scoring system extension
**Status**: Not started

### 4. Sound Effects and Music
**Reason**: Audio implementation requires:
- JavaFX Media library integration
- Audio file assets (sound effects, background music)
- Volume controls and mute functionality
- Performance considerations (audio playback overhead)
- Licensing for audio assets

**Complexity**: Low to Medium - mainly asset management
**Status**: Not started

### 5. Multiplayer Mode
**Reason**: Multiplayer would require major architectural changes:
- Network communication (sockets or web API)
- Game state synchronization
- Lobby system
- Attack mechanisms (sending garbage lines)
- Significantly increases scope beyond a refactoring project

**Complexity**: High - fundamental architecture change
**Status**: Not feasible for this project

### 6. Customizable Key Bindings
**Reason**: While WASD and arrow keys are both supported, allowing full key customization would require:
- Settings UI for key configuration
- Persistent storage of key bindings
- Key conflict detection
- Reset to defaults option

**Complexity**: Low to Medium - mainly UI and persistence
**Status**: Not started (current controls deemed sufficient)

### 7. Different Game Modes
**Reason**: Alternative modes (e.g., Sprint mode, Marathon mode, Zen mode) would need:
- Mode selection UI
- Different win/lose conditions per mode
- Mode-specific scoring and progression
- Additional testing for each mode

**Complexity**: Medium - multiple feature implementations
**Status**: Not started

**Why These Weren't Prioritized**: The focus was on getting the core refactoring work done properly (SOLID principles, design patterns) and implementing the most impactful gameplay features first. The features that were implemented are fully working and tested.

---

## New Java Classes

### Application Entry Point
1. **`com.comp2042.app.Main`**
   - Application entry point
   - Initializes JavaFX application and launches GUI
   - Sets up dependency injection (creates Board, GameController)

### UI Package (`com.comp2042.ui`)
2. **`com.comp2042.ui.InputHandler`**
   - Handles all keyboard input events
   - Processes arrow keys, WASD, Space, P, N keys
   - Delegates to event listener callbacks
   
3. **`com.comp2042.ui.GameTimer`**
   - Manages game loop timing (JavaFX Timeline)
   - Controls automatic piece dropping
   - Adjusts speed based on current level
   
4. **`com.comp2042.ui.GameViewModel`**
   - Handles data-to-UI transformations
   - Positions brick panels and updates rectangle colors
   - Calculates ghost piece visual positions
   
5. **`com.comp2042.ui.ColorMapper`**
   - Maps color codes to Paint objects
   - Provides ghost fill colors (semi-transparent)
   - Uses ColorStrategyRegistry for polymorphic color selection
   
6. **`com.comp2042.ui.NotificationPanel`**
   - Displays score and level-up notifications
   - Animated fade-in/fade-out effects
   
7. **`com.comp2042.ui.PausePanel`**
   - Pause screen with interactive buttons
   - "Resume (P)" and "New Game (N)" buttons
   
8. **`com.comp2042.ui.GameOverPanel`**
   - Game over screen with restart option

9. **`com.comp2042.ui.MainMenuPanel`**
   - Main menu screen with title and navigation options
   - "Start Game" and "Controls" buttons

10. **`com.comp2042.ui.ControlsPanel`**
    - Comprehensive game controls reference screen
    - Categorized control explanations with proper styling
   - Reflection effect for visual polish

### Color Strategy Package (`com.comp2042.ui.color`)
9. **`com.comp2042.ui.color.ColorStrategy`** (interface)
   - Strategy interface for color mapping
   - Methods: `getColorCode()`, `getColor()`
   
10. **`com.comp2042.ui.color.ColorStrategyRegistry`**
    - Registry pattern for managing color strategies
    - HashMap-based O(1) strategy lookup
    
11-19. **Color Strategy Implementations**:
    - `TransparentColorStrategy` (code 0)
    - `AquaColorStrategy` (code 1)
    - `BlueVioletColorStrategy` (code 2)
    - `DarkGreenColorStrategy` (code 3)
    - `YellowColorStrategy` (code 4)
    - `RedColorStrategy` (code 5)
    - `BeigeColorStrategy` (code 6)
    - `BurlyWoodColorStrategy` (code 7)
    - `WhiteColorStrategy` (default/fallback)

### Theme Package (`com.comp2042.ui.theme`)
20. **`com.comp2042.ui.theme.Theme`** (interface)
    - Interface defining the contract for visual themes
    - Methods: `getStylesheet()`, `getColorPalette()`, `getThemeName()`
    - Enables Strategy pattern for visual variations
    
21. **`com.comp2042.ui.theme.ThemeManager`**
    - Singleton class managing the currently active theme
    - Notifies UI when theme changes (Observer pattern)
    - Provides global access point for theme state
    
22. **`com.comp2042.ui.theme.ClassicTheme`**
    - Concrete implementation of the original visual style
    - Default theme with standard colors and styling
    
23. **`com.comp2042.ui.theme.RetroTheme`**
    - Concrete implementation of monochrome green Gameboy-style theme
    - Nostalgic handheld console aesthetic
    
24. **`com.comp2042.ui.theme.NeonTheme`**
    - Concrete implementation of high-contrast dark mode
    - Glowing colors with intended dark background

### Board Package (`com.comp2042.game.board`)
25. **`com.comp2042.game.board.Board`** (interface)
    - Main board interface (extends all segregated interfaces)
    
26. **`com.comp2042.game.board.MovableBoard`** (interface)
    - Movement operations interface (ISP)
    
27. **`com.comp2042.game.board.ClearableBoard`** (interface)
    - Row clearing operations interface (ISP)
    
28. **`com.comp2042.game.board.SpawnableBoard`** (interface)
    - Brick spawning operations interface (ISP)
    
29. **`com.comp2042.game.board.ScorableBoard`** (interface)
    - Score access interface (ISP)
    
30. **`com.comp2042.game.board.SimpleBoard`**
    - Concrete board implementation
    - Manages game state (brick position, board matrix, score)
    - Implements ghost piece calculation

### Bricks Package (`com.comp2042.game.bricks`)
31. **`com.comp2042.game.bricks.Brick`** (interface)
    - Brick interface defining shape and color methods
    
32. **`com.comp2042.game.bricks.BrickGenerator`** (interface)
    - Generator interface for producing bricks
    
33. **`com.comp2042.game.bricks.BrickGeneratorFactory`**
    - Factory for creating brick generators
    - Decouples board from concrete generator
    
34. **`com.comp2042.game.bricks.RandomBrickGenerator`**
    - Random brick generation implementation
    - Manages current and next brick
    
35-41. **Brick Implementations**:
    - `IBrick` (I-shaped tetromino)
    - `JBrick` (J-shaped tetromino)
    - `LBrick` (L-shaped tetromino)
    - `OBrick` (O-shaped/square tetromino)
    - `SBrick` (S-shaped tetromino)
    - `TBrick` (T-shaped tetromino)
    - `ZBrick` (Z-shaped tetromino)

### Controller Package (`com.comp2042.game.controller`)
42. **`com.comp2042.game.controller.GameController`**
    - Main game controller
    - Coordinates between board and UI
    - Event handling and game flow management

### Commands Package (`com.comp2042.game.controller.commands`)
43. **`com.comp2042.game.controller.commands.MoveCommand`** (interface)
    - Command interface for move operations
    
44-48. **Command Implementations**:
    - `LeftMoveCommand` - Move piece left
    - `RightMoveCommand` - Move piece right
    - `RotateMoveCommand` - Rotate piece
    - `DownMoveCommand` - Move piece down (with landing logic)
    - `HardDropMoveCommand` - Instant drop with bonus points

### Data Package (`com.comp2042.game.data`)
49. **`com.comp2042.game.data.ViewData`**
    - Immutable DTO for brick view information
    - Contains position (x, y), shape, ghost position
    
50. **`com.comp2042.game.data.DownData`**
    - Immutable DTO for down move results
    - Contains moved status, cleared status, rows cleared
    
51. **`com.comp2042.game.data.ClearRow`**
    - Immutable DTO for row clearing results
    - Contains removed row count and new board matrix
    
52. **`com.comp2042.game.data.NextShapeInfo`**
    - Immutable DTO for next piece preview data

**Additional DTOs for time control/undo**
- **`com.comp2042.game.data.BoardStateSnapshot`**
  - Immutable snapshot (board matrix, active/held pieces, queue, score, lines, level, offset, rotation) used for undo
  - Enables full state restoration when undoing the last move
- **`com.comp2042.game.data.UndoData`**
  - Result wrapper for undo operations (performed flag, view data, board matrix)

### Events Package (`com.comp2042.game.events`)
53. **`com.comp2042.game.events.InputEventListener`** (interface)
    - Event listener interface for input events
    
54. **`com.comp2042.game.events.MoveEvent`**
    - Event object for move actions
    
55. **`com.comp2042.game.events.EventType`** (enum)
    - Enumeration of event types (LEFT, RIGHT, UP, DOWN, HARD_DROP)
    
56. **`com.comp2042.game.events.EventSource`** (enum)
    - Enumeration of event sources (KEYBOARD, TIMER)

### Operations Package (`com.comp2042.game.operations`)
57. **`com.comp2042.game.operations.MatrixOperations`**
    - Utility class for matrix operations
    - Collision detection, merging, row clearing, copying
    
58. **`com.comp2042.game.operations.BrickRotator`**
    - Handles brick rotation logic
    - Calculates next shape after rotation

### Score Package (`com.comp2042.game.score`)
59. **`com.comp2042.game.score.Score`**
    - Score management with JavaFX property
    - Observable score for UI binding
    
60. **`com.comp2042.game.score.HighScoreManager`**
    - High score tracking and persistence
    - Automatic detection of new high scores
    - File I/O for saving/loading high scores

### Level Package (`com.comp2042.game.level`)
61. **`com.comp2042.game.level.Level`**
    - Immutable level configuration object
    - Contains level number, drop speed, score multiplier
    
62. **`com.comp2042.game.level.LinesClearedTracker`**
    - Tracks total lines cleared
    - Observable property for reactive updates
    
63. **`com.comp2042.game.level.LevelStrategy`** (interface)
    - Strategy interface for level configuration
    
64. **`com.comp2042.game.level.DefaultLevelStrategy`**
    - Default implementation providing predefined levels
    - Defines 10 levels with increasing difficulty
    
65. **`com.comp2042.game.level.LevelManager`**
    - Manages level progression
    - Updates level every 5 lines cleared
    - Provides current level configuration

### Test Classes
66-100. **JUnit Test Suite** (`src/test/java/com/comp2042/`)
    - `MatrixOperationsTest`, `BrickRotatorTest`
    - `ScoreTest`, `HighScoreManagerTest`
    - All 7 brick tests (IBrickTest, JBrickTest, etc.)
    - `RandomBrickGeneratorTest`, `BrickGeneratorFactoryTest`
    - `SimpleBoardTest`, `GhostBlockTest`
    - `GameControllerTest`
    - All 5 command tests (LeftMoveCommandTest, RightMoveCommandTest, etc.)
    - All 4 data tests (ViewDataTest, DownDataTest, ClearRowTest, NextShapeInfoTest)
    - All 3 event tests (MoveEventTest, EventTypeTest, EventSourceTest)
    - All 4 level tests (LevelTest, LevelManagerTest, DefaultLevelStrategyTest, LinesClearedTrackerTest)
    - `ColorMapperTest` - Tests for color code mapping and ghost colors
    - `ColorStrategyRegistryTest` - Tests for strategy lookup and fallback behavior
    - All ColorStrategy implementation tests (9 strategy classes tested)

**Total New Classes**: Over 100 Java files including source files and test files (5 theme-related classes added)

---

## Modified Java Classes

### Core Modifications

1. **`com.comp2042.ui.GuiController`**
   - **Changes**: 
     - Decomposed from 200+ lines to focused responsibilities
     - Extracted `InputHandler`, `GameTimer`, `GameViewModel`, `ColorMapper`
     - Added ghost piece rendering logic (`positionGhostPanel()`, `updateGhostRectangles()`)
     - Added high score binding (`bindHighScore()`)
     - Added level binding and level-up notifications
     - Added pause/resume functionality (`togglePause()`, `showPauseNotification()`)
     - Enhanced game over logic with high score notification
     - Improved UI refresh callback mechanism for instant updates
     - Fixed `hidePauseNotification()` to only remove pause panel instead of clearing all children
     - Increased brick size from 20 to 24 pixels for better visibility
     - Added game over overlay field for screen dimming effect
     - Adjusted ghost panel Y offset to accommodate larger bricks
     - **Visual Feedback Effects**: Added `triggerShockwaveEffect()` for board border flash on line clears
     - Added `triggerBrickBounceEffect()` and `applyBounceToMatrix()` for per-brick stroke animations
     - Updated `moveDown()` and `handleHardDrop()` to trigger visual effects when lines are cleared
     - **Theme System**: Added `initThemeHandling()` to listen for theme changes, swap CSS stylesheets dynamically, and trigger full board repaint
     - Enables runtime visual theme switching without application restart
     - **Next Piece Preview**: Added `@FXML` references for `nextBrickPanel` and `nextBrickContainer`, `nextBrickRectangles` array for preview rendering
     - Added `initNextBrickPreview()` to initialize preview panel on game start, `updateNextBrickPreview()` to refresh when new pieces spawn
     - Integrated preview updates into `refreshBrick()` for synchronized updates with active piece
   - **Reason**: Enforce Single Responsibility Principle, improve testability, support new features, enhance visual presentation, provide satisfying visual feedback, enable dynamic theme switching, and add strategic next piece preview
   
2. **`com.comp2042.game.board.SimpleBoard`**
   - **Changes**:
     - Renamed `createNewBrick()` → `trySpawnNewBrick()` (clarity)
     - Added `findDropY()` method for ghost piece calculation
     - Updated `getViewData()` to include ghost position (ghostX, ghostY)
     - Integrated `LevelManager` and `LinesClearedTracker`
     - Added `HighScoreManager` integration
     - Enhanced `newGame()` to reset level and high score trackers
     - Modified constructor to use `BrickGeneratorFactory`
   - **Reason**: Support ghost piece feature, level system, high score tracking, dependency injection
   
3. **`com.comp2042.game.data.ViewData`**
   - **Changes**:
     - Added `ghostX` and `ghostY` fields
     - Updated constructor to accept ghost position parameters
     - Added `getGhostX()` and `getGhostY()` accessor methods
   - **Reason**: Pass ghost piece position from board to UI layer (separation of concerns)
   
4. **`com.comp2042.game.controller.GameController`**
   - **Changes**:
     - Modified constructor to accept `Board` interface (dependency injection)
     - Removed direct instantiation of `SimpleBoard`
     - Added `onHardDropEvent()` method for hard drop handling
     - Added level binding logic (binds `LevelManager` to `GuiController`)
     - Added high score binding logic
     - Enhanced event handling to support new controls (Space for hard drop)
   - **Reason**: Dependency inversion, support hard drop feature, integrate level system and high score
   
5. **`com.comp2042.game.controller.commands.DownMoveCommand`**
   - **Changes**:
     - Added level-based score multiplier application
     - Integrated `LinesClearedTracker` to track cleared lines
     - Added `LevelManager.updateLevel()` call on line clears
     - Added level-up notification triggering
   - **Reason**: Integrate level system, apply progressive score multipliers
   
6. **`src/main/resources/gameLayout.fxml`**
   - **Changes**:
     - Added `<GridPane fx:id="ghostPanel">` for ghost piece rendering
     - Added `<Label fx:id="levelLabel">` for level display
     - Added `<Label fx:id="highScoreLabel">` for high score display
     - Added `<Label fx:id="nextBrickLabel">` at position (270, 120) for "Next Piece" header
     - Added `nextBrickContainer` Pane (120×120px) to hold the preview area
     - Added `nextBrickPanel` GridPane inside container for rendering the brick preview
     - Added `<Rectangle fx:id="gameOverOverlay">` covering full window for dimming effect
     - Ensured `BorderPane` has `fx:id="gameBoard"` for visual effect style class toggling (shockwave)
     - Removed previous `energyLabel` - HUD now shows only Score, Level, High Score, and Next Piece
     - Moved `GameOverPanel` from Group to root Pane for proper z-ordering
     - Scaled all UI positions by 20% to accommodate larger game elements
     - Adjusted game panel position, notification group, and score label positions
     - Added Rectangle import for overlay support
   - **Reason**: Support ghost piece visualization, level display, high score display, next piece preview, visual feedback effects, and improve game over screen visibility
   
7. **`src/main/resources/window_style.css`**
   - **Changes**:
     - Added `.game-button` style class for interactive buttons
     - Added hover and pressed state styles (`:hover`, `:pressed`)
     - Enhanced visual styling for pause and game over panels
     - Updated `.gameOverStyle` with white text color, semi-transparent red background, and better sizing
     - Added `.game-over-overlay` style with semi-transparent gray dimming effect
     - Added `.shockwave` style for board border flash effect on line clears (bright white drop shadow)
     - **Theme-Specific Next Piece Preview Styles**:
       - Classic theme: Semi-transparent dark background with whitesmoke border and white label text
       - Neon theme: Dark background with glowing green border and label (#00ff00)
       - Retro theme: Light green background with dark green border and label (#0f380f on #8bac0f)
     - Removed previous `.gameBoard.energy-low/mid/high` energy tier styles (no longer used)
   - **Reason**: Improve UI polish, user experience, game over screen visibility, provide visual feedback for line clears, and support theme-consistent next piece preview styling
   
8. **`pom.xml`**
   - **Changes**:
     - Updated main class path: `com.comp2042.app.Main` (was flat package)
     - Added Mockito dependencies for unit testing (version 5.14.2)
     - Configured maven-surefire-plugin for JUnit Platform
     - Added ByteBuddy experimental flag for Mockito compatibility
   - **Reason**: Support package reorganization, enable comprehensive testing with mocking

### Minor Modifications

9. **`com.comp2042.ui.GameOverPanel`**
   - **Changes**: 
     - Added inline styling with semi-transparent dark background
     - Increased VBox spacing from 10 to 15 pixels
     - Added rounded corners and padding for better appearance
   - **Reason**: Improve game over screen visibility and polish
   
10. **`com.comp2042.app.Main`**
    - **Changes**: 
      - Increased window dimensions from 300×510 to 432×612
      - Applied 20% increase to accommodate larger brick size
      - Additional 20% width increase for better proportions
    - **Reason**: Better visibility and improved game window proportions

11. **`com.comp2042.ui.ColorMapper`**
    - **Changes**:
      - Refactored `getFillColor()` to delegate to `ThemeManager.getInstance().getCurrentTheme()` instead of static `ColorStrategyRegistry`
      - Color selection now theme-aware and updates dynamically
    - **Reason**: Enable brick colors to update based on selected theme for runtime theme switching

12. **`com.comp2042.ui.InputHandler`**
    - **Changes**:
      - Added `onThemeToggleRequested` callback parameter
      - Bound **T** key to theme toggle callback
      - Captures user input for theme switching
    - **Reason**: Enable user to switch themes during gameplay via keyboard input

13. **`com.comp2042.game.operations.MatrixOperations`**
    - **Changes**: Minor cleanup, improved method documentation
    - **Reason**: Code maintainability
   
14. **`com.comp2042.game.operations.BrickRotator`**
   - **Changes**: Enhanced documentation, maintained existing logic
   - **Reason**: Code clarity

15. **Time Manipulation & Undo Updates**
   - **`com.comp2042.game.controller.GameController`**: Tracks earned charges (3-line slow time, 5-line undo), captures pre-move snapshots, restores state on undo, exposes ability checks.
   - **`com.comp2042.ui.GuiController`**: Wires G/U keys, applies temporary drop-speed offset (+500ms for 5s), shows notifications, resets ability timers on new game/game over.
   - **`com.comp2042.ui.InputHandler`**: Adds callbacks for G (slow time) and U (undo) keys.
   - **`com.comp2042.game.board.SimpleBoard`**: Creates/restores `BoardStateSnapshot`, syncs score/lines/level/held/queue/rotation on undo.
   - **`com.comp2042.game.bricks.RandomBrickGenerator`**: Adds queue copy/restore to support undo.
   - **`com.comp2042.game.operations.BrickRotator`**: Exposes current rotation index for snapshots.
   - **`com.comp2042.game.score.Score`**, **`com.comp2042.game.level.LinesClearedTracker`**, **`com.comp2042.game.level.LevelManager`**: Add setters/getters to rehydrate state during undo.
   - **`com.comp2042.ui.ControlsPanel`**: Documents new G/U controls in the controls screen.

**Summary**: 14+ files significantly modified, with changes driven by refactoring goals (SRP, DIP, ISP), new feature requirements (ghost piece, level system, hard drop, high score, visual feedback effects, theme system, time manipulation/undo), and UI polish improvements.

---

## Unexpected Problems

### 1. Input Latency Issue
**Problem**: After implementing the `InputHandler` extraction (SRP refactoring), there was a noticeable delay between pressing a key and seeing the piece move on screen. This made the game feel unresponsive and negatively impacted gameplay.

**Root Cause**: The `InputHandler` was calling event listeners (which updated game state) but was not immediately refreshing the UI. The `ViewData` returned from movement events was being ignored, and UI updates only occurred on the next timer tick.

**Investigation Steps**:
1. Added logging to track key press → event → UI update timing
2. Discovered `refreshBrick()` was not being called immediately after move events
3. Identified that `InputHandler` had no mechanism to trigger UI updates

**Solution**:
- Added `Consumer<ViewData> onBrickMoved` callback parameter to `InputHandler`
- `InputHandler` now captures `ViewData` from event listeners and immediately calls the callback
- `GuiController` passes `this::refreshBrick` as the callback
- Result: UI updates instantly upon key press, latency eliminated

**Lesson Learned**: When refactoring and extracting classes, it's important to make sure the data flow and timing still work correctly. UI responsiveness is especially critical in games where even small delays are very noticeable.

---

### 2. Maven Test Execution Failures with Mockito
**Problem**: After adding Mockito for unit testing, tests failed to run with cryptic errors: `java.lang.IllegalArgumentException: Could not create type` and `NoClassDefFoundError: net/bytebuddy/matcher/ElementMatcher`.

**Root Cause**: Mockito uses ByteBuddy for bytecode manipulation, which requires experimental features in Java 23. The default Maven Surefire configuration did not enable these features.

**Investigation Steps**:
1. Ran tests with `-X` flag to see detailed Maven output
2. Identified ByteBuddy version mismatch warnings
3. Researched Mockito + Java 23 compatibility issues
4. Found that ByteBuddy experimental flag was required

**Solution**:
- Added `<argLine>-Dnet.bytebuddy.experimental=true</argLine>` to maven-surefire-plugin configuration in `pom.xml`
- Updated Mockito to version 5.14.2 (latest stable)
- Added `mockito-junit-jupiter` dependency for JUnit 5 integration

**Workarounds Attempted**:
- Downgrading Java version (rejected - Java 23 required for project)
- Using alternative mocking frameworks (rejected - Mockito is industry standard)
- Running tests without mocking (rejected - reduces test quality)

**Lesson Learned**: Using the latest Java version can sometimes cause compatibility issues with testing libraries. It's worth checking if there are known issues before diving too deep into debugging.

---

### 3. Unresolved Merge Conflict Markers in Code
**Problem**: After trying to merge branches, I found `<<<<<<< Updated upstream`, `=======`, and `>>>>>>> Stashed changes` markers showing up in my `GuiController.java` file. The code wouldn't compile.

**Root Cause**: I had accidentally committed the file with unresolved merge conflicts after a `git stash pop` operation went wrong.

**Solution**:
- Manually went through and removed all the conflict markers
- Chose the correct code version for each conflicted section
- Tested to make sure everything still worked
- Committed the properly merged version

**Lesson Learned**: Always check for conflict markers before committing. Running a quick search for `<<<<<<<` in your files can save a lot of headache.

---

### 4. Git Branch Name Typo
**Problem**: I was trying to push to `Additional-Features` but realized I had been working on `Addtional-Features` (missing an "i"). Git kept saying the branch didn't exist remotely.

**Root Cause**: Created the branch with a typo using `git checkout -b Addtional-Features` and didn't notice until much later.

**Solution**:
- Switched to the correct branch name
- Used `git cherry-pick` to move my commit to the right branch
- Deleted the typo branch

**Lesson Learned**: Double-check branch names when creating them. It's annoying to fix later.

---

### 5. Bricks Spawning in Wrong Position
**Problem**: New bricks were appearing in the middle of the board vertically instead of at the top where they should spawn.

**Root Cause**: The spawn position was set to `new Point(4, 10)` - the Y coordinate was way too high. The board height is 25, so Y=10 is nearly halfway down.

**Solution**:
- Changed spawn position to `new Point(4, 0)` to spawn at the top of the board
- Tested with different brick types to make sure they all spawn correctly

**Lesson Learned**: Always verify coordinate systems and initial positions, especially when working with grid-based games.

---

### 6. Pause Functionality Not Working
**Problem**: After implementing the pause feature, pressing the P key did absolutely nothing. No errors, just no response.

**Root Cause**: Two-part problem:
1. First, `InputHandler` was missing the P key handler completely
2. Even after adding it, `GuiController` wasn't passing the pause callback

**Solution**:
- Added `onPauseRequested` parameter to `InputHandler`
- Added P key binding in the input handler
- Updated `GuiController` to pass `this::togglePause` as the callback when creating `InputHandler`

**Lesson Learned**: When adding new input handling, you need to wire it through the entire chain - from the handler to the callback to the actual action.

---

### 7. High Score Not Updating Properly
**Problem**: The high score label wasn't showing any updates even though I could see the current score was higher than the saved high score.

**Root Cause**: I was calling `highScoreManager.setCurrentScore()` at the wrong point in the game flow.

**Solution**:
- Moved the call to `gameOver()` method
- Made sure it's called before checking whether a new high score was achieved
- This way the manager can properly compare and update

**Lesson Learned**: Timing matters a lot when dealing with state updates. Make sure you're setting values before checking them.

---

### 8. Game Over Panel Not Appearing
**Problem**: When bricks reached the top of the board, the game would stop but the game over panel stayed invisible against the game background.

**Root Cause**: Multiple issues:
1. `hidePauseNotification()` was using `groupNotification.getChildren().clear()` which removed everything including the game over panel
2. The game over panel lacked proper styling and contrast
3. Panel positioning wasn't explicit enough in the FXML

**Solution**:
- Fixed `hidePauseNotification()` to only remove the pause panel specifically instead of clearing all children
- Added white text color to CSS (`-fx-text-fill: white`) in the `gameOverStyle` with better padding
- Moved `GameOverPanel` outside the Group directly into the root Pane with explicit positioning (`layoutX="40"`, `layoutY="200"`)
- Added semi-transparent dark background to the panel so it's clearly visible over the game board

**Lesson Learned**: Be specific about what you're removing from UI containers. Clearing everything can have unintended consequences. Also, UI elements need proper contrast and positioning to be visible.

---

### 9. Pause Panel Stuck on Screen
**Problem**: After pausing the game and then starting a new game, the pause panel would remain visible on screen even though the game was running.

**Root Cause**: The `newGame()` method wasn't cleaning up the pause panel UI element.

**Solution**:
- Added pause panel cleanup to the `newGame()` method
- Made sure all UI state resets when starting a new game
- Set `isPause` back to false

**Lesson Learned**: When implementing a reset/new game function, make sure to clean up all UI elements and state flags.

---

### 10. Git Reset Caused Old Bugs to Reappear
**Problem**: After doing a `git reset --hard HEAD`, bugs that I thought I had fixed started appearing again.

**Root Cause**: The commit I reset to (0fc8e52) actually already had those bugs in it. I had fixed them in later commits that I just threw away.

**Solution**:
- Used `git reflog` to find the commit history
- Reset to an earlier good commit (f588fe9) that had the fixes
- More carefully chose which commit to reset to

**Lesson Learned**: Before doing a hard reset, check the commit history to make sure you're resetting to a state that actually works. `git reflog` is your friend.

---

### 11. Incomplete Merge Breaking Compilation
**Problem**: After cloning the repository fresh and running `./mvnw clean compile`, I got a compilation error. `GameController.java` was calling `getHighScoreManager()` on a `SimpleBoard` instance, but that method didn't exist in the `SimpleBoard` class.

**Root Cause**: Incomplete merge during conflict resolution. While merging the High Score feature from `Additional-Features` branch into master, `GameController.java` got updated with the new integration code, but `SimpleBoard.java` stayed in its older state from master. The older version was missing the `HighScoreManager` field and the accessor method that the controller needed.

**Solution**:
- Used `git checkout Additional-Features -- src/main/java/com/comp2042/game/board/SimpleBoard.java` to get the correct version of `SimpleBoard.java`
- Verified compilation worked with `./mvnw clean compile`
- Committed the fix with a clear message explaining what happened
- Pushed to master branch

**Lesson Learned**: When merging features, make sure all interdependent files get updated together. Testing a fresh compile after merges is crucial to catch these dependency mismatches. The master branch should always be in a compilable, working state.

---

### Summary of Problem Resolution

All of these problems were eventually resolved without compromising the code quality or functionality. The main takeaways from dealing with these issues were:

- Always check for merge conflict markers before committing - they can break your entire build
- When merging feature branches, ensure all interdependent files are updated together
- Test compilation after every merge to catch dependency mismatches early
- Input responsiveness is really important in games - even small delays are noticeable
- Double-check coordinate systems and initial positions in grid-based games
- When adding new features, make sure to wire through the entire callback chain
- UI state management requires careful attention to what gets cleared and when
- Git operations like reset and cherry-pick need to be done carefully - use `git reflog` when in doubt
- The master branch should always remain in a compilable, working state
- Testing early and understanding framework constraints saves a lot of debugging time later
- Using the latest Java version can cause some compatibility issues with testing frameworks

---

## Project Statistics

### Code Metrics
- **Total Java Files**: Over 100 files including source and test files (added 5 theme system classes)
- **Test Methods**: Comprehensive and growing test suite with 200+ test methods across 35+ test files
- **Design Patterns Used**: 6 major patterns (Strategy, Factory, Command, Observer, Registry, Singleton)
- **SOLID Principles Applied**: All 5 principles throughout codebase

### Development Timeline
- **Duration**: Development from late September through early December 2025
- **Expected Completion**: December 8, 2025
- **Total Commits**: Multiple commits across several feature branches
- **Test Coverage**: All major components covered

### Features Summary
- Package reorganization (31 files reorganized)
- Single Responsibility refactoring (7 extracted classes)
- Design patterns (5 patterns implemented)
- Interface segregation (Board split into 4 interfaces)
- Dependency injection (3 major components)
- Bug fixes (3 critical issues resolved)
- Hard drop (Space key, 2 pts/cell bonus)
- Pause/resume (P key, interactive panel)
- Level system (10 levels, adaptive difficulty)
- Ghost piece (semi-transparent preview, real-time updates)
- Next piece preview (strategic planning with real-time preview)
- High score tracking (persistent storage)
- Visual feedback effects (shockwave flash and brick bounce on line clears)
- Visual themes system (3 themes: Classic, Retro, Neon - switchable with T key)
- Comprehensive test suite (200+ tests across 35+ files)

---

## Conclusion

This project demonstrates application of SOLID principles and design patterns to transform a legacy codebase. The refactoring work has focused on improving code organization, maintainability, and extensibility while adding meaningful gameplay enhancements like the level system, ghost piece preview, next piece preview, hard drop functionality, high score tracking, visual feedback effects, and a robust visual themes system allowing runtime style switching.

All implemented features so far are fully functional and tested. The codebase is well-organized and structured for maintainability. Several unexpected technical challenges have been encountered and resolved during development, particularly around UI responsiveness, test framework compatibility, and JavaFX threading requirements.
