# Traffic Light Scheduler

A Java program that figures out which cars at an intersection can safely get a green light at the same time.

I built this for my high school data structures class.

---

## What it does

Every possible route through an intersection (like "from north, going south") is a **node** in a graph. If two routes would crash into each other, they're connected by an **edge**.

The program then uses **greedy graph coloring** to put the routes into groups. Routes in the same group never crash, so each group can be one green light.

On the test intersection, the program fits all 12 routes into **4 green-light groups**.

---

## How to use it

Run `TrafficFlow/main/Program.java`. A window opens with the intersection map.

- **A, B, C, D:** show the routes coming from that side (A = north, B = east, C = south, D = west)
- **X with one route showing:** show every route that would crash into it
- **X with nothing showing:** cycle through the green-light groups

---

## What I worked on

- `Graph.java` and `Node.java`: the graph, and the greedy coloring algorithm
- `Program.java`: building the graph from the map and the keyboard controls

The map framework (`MapFrame`, `MapImage`) and the drawing library in `lib/` were provided by my class.

The original intersection map wasn't available, so the test map (`TrafficFlow/maps/Loyal.jpg`) was generated with AI help.
---

## What I learned

- How to turn a real-world problem (traffic) into a graph
- How greedy coloring works, and why it groups things that don't conflict
- How to use a queue to keep track of what still needs a color

---

## Run it from the terminal

```bash
javac -sourcepath . -cp "lib/*" -d out $(find TrafficFlow -name "*.java")
java -cp "out:lib/*" TrafficFlow.main.Program
```