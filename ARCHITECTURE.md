
## Simple flowchart
``` mermaid
flowchart TD
    subgraph Group1[" "]
        A
    end
    B["Box\nBox2"] -->|connection name| A["File Search Engine\n[Software System]"]
    A["File Search Engine\n[Software System]"] -->|connection name 2| C["Box2\nBox3"]
   
