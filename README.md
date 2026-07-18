# QuizMaster

Welcome to **QuizMaster 2026**, a modern, premium desktop application built for the **CodeSoft Java Internship Task 4 (Quiz Application)**. 

This application demonstrates the capabilities of Java Swing to build beautiful, modern SaaS-like interfaces without relying on any external GUI frameworks like JavaFX or third-party libraries. It incorporates custom Java2D rendering for advanced Glassmorphism effects.

## 🌟 Project Features

- **Robust Quiz Engine**: Manages state, scoring, correct/wrong/skipped counts, and randomized questions dynamically.
- **Built-in Question Bank**: Contains 20+ challenging Java and Computer Science questions.
- **Live 30-Second Timer**: Features a seamless background timer that automatically auto-submits skipped questions when time expires.
- **Advanced Grading System**: Accurately calculates grades (`Excellent`, `Very Good`, `Good`, `Average`, `Needs Improvement`) and percentage.
- **Glassmorphism UI**: Beautiful semi-transparent frosted panels, glowing borders, and soft inner shadows engineered completely with standard `Java2D`.
- **Custom Components**: Includes meticulously designed `RoundedRadioButton` and `RoundedButton` components for a modern feel.
- **Dynamic Theming**: Instantly toggle between a sleek Dark Mode and a crisp Light Mode, updating components in real-time.
- **Solid Architecture**: Strictly follows OOP & SOLID principles by separating the UI (`QuizApplication`) from the Core Logic (`QuizEngine`, `QuestionBank`, `Result`).

## 📁 Folder Structure

```text
src
│
├── app
│      Main.java                 # Entry Point & JFrame setup
│
├── quiz
│      QuizApplication.java      # Master UI Router & Views
│      QuizEngine.java           # Core Game Logic & State Manager
│      QuestionBank.java         # Data Store for 20+ Questions
│
├── model
│      Question.java             # Entity representing a single Question
│      Result.java               # Entity representing Final Score Statistics
│
├── ui
│      GradientPanel.java         # High-quality Background Renderer
│      RoundedButton.java        # Interactive Animated Buttons
│      RoundedRadioButton.java   # Custom Modern Option Buttons
│      GlassPanel.java           # Frosted Glass containers
│
└── utils
       ThemeManager.java         # Singleton Color & Typography State
       TimerManager.java         # Utility for the 30-second Countdown
