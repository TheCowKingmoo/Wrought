-------------------------------------------
Wrought
-------------------------------------------

This is a Minecraft mod that is centered around the following
   1. Most Machines are Multiblocks
   2. Tech related to heavy industry(Coal Coke Production, Steel, Etc)
   3. All machines have easy to modify recipes and potentially extra input/output slots for modpack makers. 



Other Notes
- An important aspect for me is to try and make the multiblocks somewhat detailed, but to not look out of place in a Minecraft world. To acheive this I want to make the multiblock only only out blocks,slabs, stairs and not render some complex object on top of it. 
- A huge barrier from other multiblock mods is actually making the multiblock. Instead of placing each block the user can click on the controller which lets the, know all the block it still needs. If the user has all those blocks in inventory the controller will place them all for you.



Machines Implemented So Far
- Honey Comb Coke Oven
  - What is it: a furnace that requires no fuel which cooks off impurities in some items. Very similiar to Railcrafts coke oven
  - Size: 7x7x7
  - Recipes:
    - Coke + Creosote. Requires Coal
    - Charcoal + Creosote. Requires Wood Logs
- Bloomery (Very WIP)
  - What is it: Early game smelting of ores. Lets you smelt ores into thier respective ingots assuming you can get it hot enough.
  - Size: Not finalized. Currently copies Coke Oven
  - Recipes
    - Iron Ingot + Slag. Requires Iron Ore + Stone at 2947 degrees.
- Blast Furnace
  - What is it: A more efficent way of smelting ores. 
  - Size: Varies hugely. It has a 7x7 base but goes up nearly 20 blocks.
  - Recipes
    - Molten Iron. Requires Iron Ore + Limestone at 2947 degrees.
    - Currently trying to figure out a more robust way of creating all the recipes for all the different metals.
  

Machines Planned
- Casting Machine
  - What is it: A way to turn molten fluids into ingots/blocks/gears
  - Size: Unknown.
  
- Sinter Plant || Pellet Press
  - What is it: A way to pre process ores before the blast furnace in order to increase its yield
  - Size: Unknown
- Oxygen Furance
  - What is it: A furnace that blasts hot air over molten metals and is used to refine iron into steel
  - Size: Unknown
- Concentrating Mill (Crusher) -
  - What is it: Turns the Ore into Fine Dust.
- Flotation Cell -
  - Crushed Ore is diluted with water and pored into a Flotation Cell. A small amount of pine oil is added and the slurry is shaken. This makes all the waste rock drop to the bottom along with other metals not wanted.      
- Filter
  - What is it: Slurry is forced through a filter
- Drossing Kettle
  - What is it: heats up metal

Blocks
   - Honeycomb Coke Oven Controller
      - the main TE for the Coke Oven Multiblock
   - Honeycomb Coke Oven Frame Block, Stair, Slab
      - main construction block of the Coke Oven Multiblock
      - only thing this TE knows is how to get to the controller
   - Blast Furnace Controller
      - the main TE for the Blast Furnace Multiblock
   - Blast Furnace Frame Block, Stair, Slab
      - main construction block of the Blast Furnace Multiblock
      - only thing this TE knows is how to get to the controller
   - Bloomery Controller
      - the main TE for the Bloomery
   - Refactory Brick Block, Stair, Slab
      - main construction block of the Bloomery and a secondary frame block on other multiblocks
      - only thing this TE knows is how to get to the controller   
      
 Items
   - Ash 
      - byproduct of making charcoal
   - Soot
      - byproduct of making coke
   - Coke
      - A more refined version of coal

  Fluids
    - Creosote
      - A byproduct from the coke oven
    - Molten Iron
      - The molten form of iron


Current Development is being tracked through Github Issues.

Source Reading Material
https://www.steel.org/steel-technology/steel-production/
https://acoup.blog/2020/09/18/collections-iron-how-did-they-make-it-part-i-mining/

Forge Modding Guides
Unoffical Docs - https://forge.gemwire.uk
Tile Entity Sync - https://gist.github.com/Commoble/c96271da4d8cf4e33f4370eda952f210
Item Slots - https://www.youtube.com/watch?v=QUxLsZHiyA4&list=PLaevjqy3XufYmltqo0eQusnkKVN7MpTUe&index=48
