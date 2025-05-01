# pgsqlpaint frontend

This is a frontend for [lopataa/pgsqlpaint](https://github.com/lopataa/pgsqlpaint). The main focus wasn't on the
frontend, so beware that it is not the prettiest thing in the world.
In short, the project is about building a paint app in SQL. This also allows for the paint app to have multiplayer,
isn't that cool?

## Setup

1) Ensure you have the pgsqlpaint backend set up and running. You can find it
   here: [lopataa/pgsqlpaint](https://github.com/lopataa/pgsqlpaint). Follow the instructions in its README to configure
   the PostgreSQL database and any necessary extensions.
2) Download the artifact, or build and run the application.
3) Have fun.

## Functionality

All of the tools and configurable properties can be found in the menu of the application.

### Drawing canvas

Canvas is redrawn using the selected renderer method every time something changes. It listens for a `repaint`
event, meaning it repaints only if it needs to, so it saves resources. To save bandwidth it is recommended to use the
`PNG` renderer. Canvas is synchronized across all users connected to the same server.

### File

#### New Canvas

Opens a dialog where you can set the width and height of a new canvas. The current canvas will be cleared and resized to
the specified dimensions.

#### Export to PNG

Exports your current drawing as a PNG image. Uses a fast third-party Python-based renderer (`plpython3u` and `PIL`).

#### Export to BMP

Exports your drawing in BMP format. This method is slower and bandwidth-heavy but fully written in SQL.

### Color

#### Choose Color

Opens a color picker dialog. The selected color becomes your active drawing color.

### Tools Menu

These tools determine how you interact with the canvas. Only one tool can be active at a time:

#### Line

Draws straight lines.

#### Fill

Flood fill an area with the current color.

#### Draw

Freehand drawing like a pencil.

#### Text

Inserts text.

#### Move

Move a selection on canvas. Select by dragging your mouse, the selected area will be highlighted. Then drag again to
select the target area, your initial selection will get copied to this selection. Right-click to cancel the source.

#### Circle

Draw circles, by dragging.

#### Rectangle

Draw rectangles by dragging.

#### Square

Draw squares, same as rectangle tool.

#### Eraser

Erase parts of your drawing, similar to draw tool

#### Polygon

Create polygon shapes by clicking multiple points, end it by clicking near the first point.
> Your currently selected tool will remain highlighted in the menu.

### Renderer

Choose the image rendering backend for display:

#### PNG Renderer (Recommended)

- Faster and uses less bandwidth.
- Uses a third-party Python library.

#### BMP Renderer

- Slower, uses more bandwidth.
- Fully written in SQL.

### Line Style

Customize how your lines appear. This affects all shapes, lines, squares, circles etc.

- Solid – Standard continuous lines.
- Dashed – Lines with dashed segments.
- Dotted – Lines made of dots.

### Stroke Width

#### Set Stroke Width...

Opens a dialog to choose the thickness of lines, shapes, and drawing tools. Ranges from 1 to 50.