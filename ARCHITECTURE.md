
## System context
``` mermaid
flowchart TD
    subgraph Group1[" "]
        A
    end
    B["User\n[Person]\nThe user who searches for any file"] -->|Searches for a specific file or files| A["File Search Engine\n[Software System]"]
    A["File Search Engine\n[Software System]"] -->|Interacts with the computer to get the files| C["Computer"]
```

## Container diagram

``` mermaid

flowchart TD
    subgraph Group1[" "]
        A
        B
        C
    end
    D["User\n[Person]\nThe user who searches for any file"] -->|Searches for a specific file or files| C
    C["Frontend Application\n\nThe UI which the client can use to communicate with the application"] --> A["Backend Application\n\nMakes the connection between the UI and the database, performs queries,
implements the search mechanism"]
    A --> B["Database\n[Relational database schema]\nIndexes and stores file data"]
    A -->|Interacts with the computer to get the files| E["Computer"]

```

## Component diagram - Frontend App

``` mermaid
flowchart TD
    subgraph Group1[" "]
        A
    end

    C["User\n[Person]\nThe user who searches for any file"] -->|Searches for a specific file or files| A
    A["Search Controller\n\nHandles the user input and sends it to the backend"] --> |Writes to| B["Backend Application\n\nMakes the connection between the UI and the database, performs queries,
implements the search mechanism"]

```

## Component diagram - Backend App

``` mermaid
flowchart TD

    subgraph Group1[" "]
        A
        B
        C
        D
        E
        F
    end
    G["Frontend Application\n\nThe UI which the client can use to communicate with the application"] --> A
    A[Query Processor\n\nProcesses the queries coming from the front end] --> B[File Search\n\nLooks for the file using the DIM]
    C[File Traversal\n\nTraverses the system obtaining the files] --> D[Metadata Extractor]
    D-->E["File indexer\n\nFormats the file in an appropriate way and send it to the DB"]
    E-->F["Database interaction module\n\nInteracts with the DB for updating, deleting, searching, etc."]
    B-->F
    F-->H["Database\n[Relational database schema]\nIndexes and stores file data"]
    C -->|Interacts with the computer to get the files| I["Computer"]
    


```
## Class diagram - Frontend App

``` mermaid
classDiagram
    namespace Search Controller {
        class SearchController {
            +sendUserInput(): void
        }
    }
```


   
