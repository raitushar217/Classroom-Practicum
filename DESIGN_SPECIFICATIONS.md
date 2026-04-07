# Design Specifications - Reater App (Jetpack Compose)

## 🎨 Visual Design System

### Color Palette

#### Primary Colors
```
Primary:           #6750A4  (Purple - Main brand color)
On Primary:        #FFFFFF  (White text on primary)
Primary Container: #EADDFF  (Light purple for badges)
On Primary Container: #21005D (Dark purple text)
```

#### Secondary Colors
```
Secondary:         #625B71  (Gray purple)
On Secondary:      #FFFFFF  (White text on secondary)
Secondary Container: #E8DEF8 (Light gray purple)
On Secondary Container: #1D192B (Dark text)
```

#### Tertiary Colors
```
Tertiary:          #7D5260  (Mauve)
On Tertiary:       #FFFFFF  (White text)
Tertiary Container: #FFD8E4 (Light pink)
On Tertiary Container: #31111D (Dark text)
```

#### Surface & Background
```
Background:        #FFFBFE  (Off-white)
On Background:     #1C1B1F  (Almost black)
Surface:           #FFFBFE  (Off-white)
On Surface:        #1C1B1F  (Almost black)
Surface Variant:   #E7E0EC  (Light gray)
On Surface Variant: #49454F (Medium gray)
```

#### Outline & Dividers
```
Outline:           #79747E  (Medium gray)
Outline Variant:   #CAC4D0  (Light gray)
Divider:           #E8E8E8  (Very light gray)
```

### Typography Scale

#### Display Styles (Large headings)
```
Display Large:
  Size: 57sp
  Line Height: 64sp
  Weight: Normal
  Letter Spacing: -0.25sp

Display Medium:
  Size: 45sp
  Line Height: 52sp
  Weight: Normal
  Letter Spacing: 0sp

Display Small:
  Size: 36sp
  Line Height: 44sp
  Weight: Normal
  Letter Spacing: 0sp
```

#### Headline Styles (Section headers)
```
Headline Large:
  Size: 32sp
  Line Height: 40sp
  Weight: SemiBold
  Letter Spacing: 0sp

Headline Medium:
  Size: 28sp
  Line Height: 36sp
  Weight: SemiBold
  Letter Spacing: 0sp

Headline Small:
  Size: 24sp
  Line Height: 32sp
  Weight: SemiBold
  Letter Spacing: 0sp
```

#### Title Styles (Card titles, toolbar)
```
Title Large:
  Size: 22sp
  Line Height: 28sp
  Weight: SemiBold
  Letter Spacing: 0sp
  Usage: Top app bar, card titles

Title Medium:
  Size: 16sp
  Line Height: 24sp
  Weight: SemiBold
  Letter Spacing: 0.15sp
  Usage: Card subtitles, section headers

Title Small:
  Size: 14sp
  Line Height: 20sp
  Weight: Bold
  Letter Spacing: 0.1sp
  Usage: Small headers, emphasized text
```

#### Body Styles (Main content)
```
Body Large:
  Size: 16sp
  Line Height: 24sp
  Weight: Normal
  Letter Spacing: 0.15sp
  Usage: Main content, descriptions

Body Medium:
  Size: 14sp
  Line Height: 20sp
  Weight: Normal
  Letter Spacing: 0.25sp
  Usage: Secondary content, metadata

Body Small:
  Size: 12sp
  Line Height: 16sp
  Weight: Normal
  Letter Spacing: 0.4sp
  Usage: Captions, helper text
```

#### Label Styles (Buttons, badges)
```
Label Large:
  Size: 14sp
  Line Height: 20sp
  Weight: SemiBold
  Letter Spacing: 0.1sp
  Usage: Button text, prominent labels

Label Medium:
  Size: 12sp
  Line Height: 16sp
  Weight: SemiBold
  Letter Spacing: 0.5sp
  Usage: Badges, chips

Label Small:
  Size: 11sp
  Line Height: 16sp
  Weight: SemiBold
  Letter Spacing: 0.5sp
  Usage: Small badges, tags
```

### Spacing System (8dp Grid)

```
Extra Small:  4dp   (Tight spacing within components)
Small:        8dp   (Default spacing between elements)
Medium:       12dp  (Spacing between related groups)
Large:        16dp  (Padding inside cards, screen margins)
Extra Large:  24dp  (Section spacing)
XXL:          32dp  (Major section breaks)
```

#### Common Spacing Patterns
```
Card Padding:           16dp
Card Spacing:           12dp (between cards)
Screen Horizontal:      16dp
Screen Vertical:        8dp
Icon-Text Spacing:      8dp
Section Header Margin:  8dp (top/bottom)
```

### Corner Radius

```
Extra Small:  6dp   (Small badges, tags)
Small:        8dp   (Badges, chips)
Medium:       12dp  (Info cards, buttons)
Large:        16dp  (Main cards)
Extra Large:  20dp  (Featured cards)
Full:         28dp  (Search bar, pill buttons)
Circle:       50%   (Profile avatars, icon buttons)
```

### Elevation & Shadows

```
Level 0:  0dp   (Flat surfaces, no shadow)
Level 1:  1dp   (Subtle elevation)
Level 2:  2dp   (Pressed state)
Level 3:  3dp   (Raised cards)
Level 4:  4dp   (Floating action button)
Level 5:  8dp   (Dialogs, menus)
```

#### Card Elevation
```
Default:  0dp   (Flat, minimalistic)
Pressed:  2dp   (When user taps)
Hover:    1dp   (On hover, if applicable)
```

### Icon Sizes

```
Extra Small:  16dp  (Inline icons)
Small:        18dp  (List item icons)
Medium:       20dp  (Card icons)
Large:        24dp  (Primary action icons)
Extra Large:  40dp  (Profile icons)
Hero:         48dp  (Large feature icons)
```

## 📐 Component Specifications

### Home Screen

#### Top App Bar
```
Height: 64dp
Background: Background color (#FFFBFE)
Elevation: 0dp

Title:
  - Greeting: Title Large (22sp, SemiBold)
  - Subtitle: Body Medium (14sp, Normal)
  - Color: On Background (#1C1B1F)

Profile Button:
  - Size: 40dp circle
  - Background: Primary Container (#EADDFF)
  - Icon: 24dp, On Primary Container color
  - Margin Right: 16dp
```

#### Search Bar
```
Height: 56dp
Corner Radius: 28dp (full pill shape)
Padding: 16dp horizontal
Background: Surface (#FFFBFE)
Border: 1dp, Outline Variant (#CAC4D0)

Icon:
  - Size: 24dp
  - Color: On Surface Variant (#49454F)
  - Margin Right: 8dp

Text:
  - Style: Body Medium (14sp)
  - Color: On Surface (#1C1B1F)
  - Placeholder Color: On Surface Variant (#49454F)
```

#### Class Card
```
Width: Match parent
Corner Radius: 16dp
Padding: 16dp
Background: Surface (#FFFBFE)
Elevation: 0dp (default), 2dp (pressed)
Margin Bottom: 12dp

Subject ID Badge:
  - Corner Radius: 8dp
  - Padding: 12dp horizontal, 6dp vertical
  - Background: Primary Container (#EADDFF)
  - Text: Label Medium (12sp, SemiBold)
  - Color: On Primary Container (#21005D)

Subject Name:
  - Style: Title Large (22sp, SemiBold)
  - Color: On Surface (#1C1B1F)
  - Max Lines: 2
  - Margin Top: 12dp

Info Rows:
  - Icon Size: 18dp
  - Icon Color: On Surface Variant (#49454F)
  - Text Style: Body Medium (14sp)
  - Text Color: On Surface Variant (#49454F)
  - Spacing: 8dp between icon and text
  - Margin: 8dp between rows

Credits Badge:
  - Corner Radius: 6dp
  - Padding: 8dp horizontal, 4dp vertical
  - Background: Secondary Container (#E8DEF8)
  - Text: Label Small (11sp, SemiBold)
  - Color: On Secondary Container (#1D192B)
  - Margin Top: 12dp
```

### Profile Screen

#### Profile Header Card
```
Width: Match parent
Corner Radius: 20dp
Padding: 24dp
Background: Primary Container (#EADDFF)
Elevation: 0dp

Avatar:
  - Size: 80dp circle
  - Background: Primary (#6750A4)
  - Icon Size: 40dp
  - Icon Color: On Primary (#FFFFFF)

Name:
  - Style: Headline Small (24sp, Bold)
  - Color: On Primary Container (#21005D)
  - Margin Top: 16dp

Enrollment ID:
  - Style: Body Medium (14sp)
  - Color: On Primary Container with 70% opacity
  - Margin Top: 4dp
```

#### Section Header
```
Text Style: Title Medium (16sp, SemiBold)
Color: On Background (#1C1B1F)
Margin: 8dp vertical
```

#### Info Card
```
Width: Match parent
Corner Radius: 12dp
Padding: 16dp
Background: Surface (#FFFBFE)
Elevation: 0dp
Margin Bottom: 16dp

Icon Container:
  - Size: 40dp
  - Corner Radius: 10dp
  - Background: Secondary Container (#E8DEF8)
  - Icon Size: 20dp
  - Icon Color: On Secondary Container (#1D192B)

Label:
  - Style: Label Medium (12sp, SemiBold)
  - Color: On Surface Variant (#49454F)

Value:
  - Style: Body Large (16sp)
  - Color: On Surface (#1C1B1F)
  - Margin Top: 4dp

Spacing:
  - Icon to Text: 16dp
```

### Quiz Screen

#### Tab Row
```
Height: 48dp
Background: Surface (#FFFBFE)
Indicator Color: Primary (#6750A4)
Indicator Height: 3dp
Margin: 16dp horizontal

Tab:
  - Text Style: Title Small (14sp)
  - Selected Weight: SemiBold
  - Unselected Weight: Normal
  - Selected Color: Primary (#6750A4)
  - Unselected Color: On Surface Variant (#49454F)
  - Padding: 16dp horizontal
```

#### Quiz Card
```
Width: Match parent
Corner Radius: 16dp
Padding: 16dp
Background: Surface (#FFFBFE)
Elevation: 0dp (default), 2dp (pressed)
Margin Bottom: 12dp

Icon Container:
  - Size: 48dp
  - Corner Radius: 12dp
  - Background: Primary Container (#EADDFF)
  - Icon Size: 24dp
  - Icon Color: On Primary Container (#21005D)

Quiz Name:
  - Style: Title Medium (16sp, SemiBold)
  - Color: On Surface (#1C1B1F)
  - Max Lines: 2

Syllabus:
  - Style: Body Small (12sp)
  - Color: On Surface Variant (#49454F)
  - Margin Top: 4dp

Divider:
  - Color: Outline Variant (#CAC4D0)
  - Thickness: 1dp
  - Margin: 16dp vertical

Detail Sections:
  - Icon Size: 16dp
  - Icon Color: On Surface Variant (#49454F)
  - Label Style: Label Small (11sp, SemiBold)
  - Value Style: Body Medium (14sp)
  - Spacing: 6dp between icon and label, 4dp between label and value

Question Badge:
  - Corner Radius: 8dp
  - Padding: 12dp horizontal, 6dp vertical
  - Background: Tertiary Container (#FFD8E4)
  - Text: Label Medium (12sp, SemiBold)
  - Color: On Tertiary Container (#31111D)
  - Margin Top: 12dp
```

### Empty States

```
Container:
  - Alignment: Center
  - Padding: 16dp

Primary Text:
  - Style: Body Large (16sp)
  - Color: On Surface Variant (#49454F)

Secondary Text:
  - Style: Body Medium (14sp)
  - Color: On Surface Variant with 70% opacity
  - Margin Top: 8dp
```

### Loading State

```
Indicator:
  - Size: 48dp
  - Color: Primary (#6750A4)
  - Stroke Width: 4dp
  - Alignment: Center of screen
```

## 🎭 Interaction States

### Clickable Items
```
Default:
  - Elevation: 0dp
  - Background: Surface

Pressed:
  - Elevation: 2dp
  - Background: Surface
  - Ripple Color: Primary with 12% opacity

Disabled:
  - Opacity: 38%
  - No ripple effect
```

### Text Fields
```
Unfocused:
  - Border: 1dp, Outline Variant (#CAC4D0)
  - Background: Surface (#FFFBFE)

Focused:
  - Border: 2dp, Primary (#6750A4)
  - Background: Surface (#FFFBFE)

Error:
  - Border: 2dp, Error color
  - Helper text in error color
```

## 📱 Responsive Breakpoints

```
Compact:  < 600dp width  (Phones)
Medium:   600-840dp      (Tablets, foldables)
Expanded: > 840dp        (Large tablets, desktop)
```

### Adaptive Layouts
```
Compact:
  - Single column
  - Full-width cards
  - Bottom navigation

Medium:
  - Two columns for cards
  - Side navigation rail
  - Increased padding (24dp)

Expanded:
  - Three columns for cards
  - Permanent navigation drawer
  - Maximum content width: 1200dp
```

## ♿ Accessibility

### Minimum Touch Targets
```
Minimum Size: 48dp x 48dp
Recommended: 48dp x 48dp for all interactive elements
```

### Content Descriptions
```
All icons must have contentDescription
Decorative icons: contentDescription = null
Interactive elements: Descriptive text
```

### Color Contrast Ratios
```
Normal Text: 4.5:1 minimum
Large Text: 3:1 minimum
UI Components: 3:1 minimum
```

### Text Scaling
```
Support: Up to 200% text scaling
Test: All layouts with large text enabled
Ensure: No text truncation or overlap
```

## 🎬 Animation Specifications

### Duration
```
Fast:     100-200ms  (Small changes, ripples)
Medium:   200-300ms  (Standard transitions)
Slow:     300-500ms  (Complex transitions)
```

### Easing
```
Standard:     Cubic bezier (0.4, 0.0, 0.2, 1)
Decelerate:   Cubic bezier (0.0, 0.0, 0.2, 1)
Accelerate:   Cubic bezier (0.4, 0.0, 1, 1)
```

### Common Animations
```
Fade In/Out:  200ms, Standard easing
Slide:        300ms, Decelerate easing
Scale:        200ms, Standard easing
Ripple:       150ms, Accelerate easing
```

## 📏 Layout Guidelines

### Screen Padding
```
Horizontal: 16dp
Vertical: 8dp (top), 16dp (bottom)
```

### Content Spacing
```
Between Cards: 12dp
Between Sections: 24dp
Within Cards: 16dp
```

### Maximum Widths
```
Content: 1200dp (for large screens)
Cards: Match parent with margins
Text: 600dp (for readability)
```

---

**Design System Version**: 1.0.0  
**Last Updated**: April 2026  
**Based on**: Material Design 3 Guidelines  
**Platform**: Android (Jetpack Compose)
