# Smart Pantry Manager

## 1. Project Overview
Smart Pantry Manager is an Android application developed in Java using XML layouts. Designed as a practical mobile solution, the app enables users to organize household ingredients, monitor expiration dates, and discover recipes that match their current inventory. The primary goal of the application is to reduce food waste and help users make the most of their available food resources.

---

## 2. Key Features

### Pantry Management
* **Add Ingredients:** Input items with details including name, quantity, unit, and optional expiration date.
* **Input Validation:** Validates ingredient entries prior to saving.
* **View Inventory:** Clear overview of all items currently available in the pantry.
* **Manage Items:** Easily edit or delete ingredient records as inventory changes.

### Recipe Collection
* **Preloaded Recipes:** Browse a library of pre-configured recipes.
* **Recipe Details:** View complete ingredient lists and step-by-step preparation instructions.
* **Local Caching:** Store recipe data locally using SQLite.

### Suggested Recipes & Matching Logic
* **Smart Recommendations:** Suggests recipes that can be made using available pantry ingredients.
* **Strict Matching:** Filters out recipes that require missing ingredients.
* **Quantity & Unit Handling:** Evaluates ingredient quantities and reconciles common unit differences.
* **Name Normalization:** Handles basic variations in ingredient names (such as singular vs. plural forms).

### Settings & Preferences
* **Unit Preferences:** Switch between Metric and Imperial measurement systems.
* **Expiration Alerts:** Configure custom notification times for expiring items.

### Data Persistence
* **Offline Storage:** Saves pantry inventory and recipe data in a local SQLite database.
* **Session Persistence:** Ensures all data remains saved even when the app is closed and restarted.

---

## 3. Technologies Used

* **Programming Language:** Java
* **Target Android Version:** 10.1
* **User Interface:** XML Layouts & `RecyclerView` with custom adapters
* **Navigation:** Android Activities & Intents
* **Database & Persistence:** SQLite managed via `SQLiteOpenHelper`
* **Version Control:** Git & GitHub

---

## 4. Database Architecture

SQLite was chosen as the primary database solution because it provides a reliable, local persistent store that operates without requiring a remote server or internet connection. 

The app utilizes SQLite to cache pantry inventory and recipe records locally. Schema creation and upgrades are handled using `SQLiteOpenHelper`, while data operations are encapsulated within dedicated Repository classes. This architecture is lightweight and ideal for managing personal pantry data directly on an Android device.

---

## 5. Project Structure

The codebase is organized into modular classes and packages based on functionality:

* `AddEditIngredientActivity.java` — Manages adding and editing pantry ingredients.
* `PantryActivity.java` — Displays the list of current pantry ingredients.
* `PantryAdapter.java` — Binds pantry data to the `RecyclerView` user interface.
* `RecipeDetailActivity.java` — Displays ingredients and step-by-step instructions for selected recipes.
* `RecipeMatcher.java` — Contains logic to evaluate available ingredients against recipe requirements.
* `RecipeRepository.java` — Handles database operations and queries for recipe records.
* `PantryRepository.java` — Handles database CRUD operations for pantry data.
* `DatabaseHelper.java` — Manages SQLite database creation, schema setup, and version upgrades.
* `UnitConverter.java` — Performs conversions between Metric and Imperial measurements.
* `SettingsActivity.java` — Manages user preferences and app settings.

---

## 6. Requirements

To open and run this project, ensure you have the following installed:
* **Android Studio**
* **Compatible Android SDK** (installed via Android Studio)
* **Android Emulator** or a compatible physical Android device

---

## 7. Setup & Run Instructions

1. **Clone or Download:** Clone the repository or download the source code files.
   ```bash
   git clone [https://github.com/NqobaMhlongo/MAD700.git](https://github.com/NqobaMhlongo/MAD700.git)