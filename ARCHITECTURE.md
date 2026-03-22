
## System context
``` mermaid
flowchart TD
    subgraph Group1[" "]
        A
    end
    B["User\n[Person]"] -->|Searches for a specific file or files| A["File Search Engine\n[Software System]"]
    A["File Search Engine\n[Software System]"] -->|Gets the information from| C["Computer"]
   
