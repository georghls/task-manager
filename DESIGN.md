---
name: Task Manager
description: Ajudar os usuários a organizar sua rotina e afazeres diários com facilidade
colors:
  primary: "#79addc"
  neutral-bg: "#fcf5c7"
  neutral-surface: "#ffffff"
  success: "#adf7b6"
  pending: "#ffee93"
  text-primary: "#27272a"
typography:
  display:
    fontFamily: "Playfair Display, serif"
  body:
    fontFamily: "Lora, serif"
  mono:
    fontFamily: "Courier Prime, monospace"
rounded:
  sm: "4px"
  md: "8px"
  lg: "12px"
spacing:
  md: "16px"
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.sm}"
    padding: "8px 16px"
---

# Design System: Task Manager

## Overview

**Creative North Star: "Pastel Vintage Brutalism"**

Uma interface com a força tátil do brutalismo plano (hard shadows, bordas grossas sólidas), mas colorizada com tons pastéis "Warm Clarity" para suavizar o impacto e trazer conforto no uso diário. O estilo estrutural foi mantido a pedido do usuário.

**Key Characteristics:**
- Tons pastéis quentes e aconchegantes.
- Presença forte de bordas sólidas grossas e hard shadows.
- Tipografia vintage (Playfair Display, Lora, Courier Prime).

## Colors

### Primary
- **Azul Pastel** (#79addc): Usado na principal Call to Action. Uma cor suave, contrastando com a borda escura da interface.

### Secondary
- **Pêssego** (#ffc09f): Cor secundária, quente.
- **Verde Pastel** (#adf7b6): Usado para tarefas concluídas.
- **Amarelo Pastel** (#ffee93): Usado para marcações pendentes ou alertas leves.

### Neutral
- **Creme Claro** (#fcf5c7): O fundo primário, super suave.
- **Espresso** (#27272a): O texto e borda principal.

## Typography

**Display Font:** Playfair Display, serif
**Body Font:** Lora, serif
**Label/Mono Font:** Courier Prime, monospace

## Layout

O container central (1100px max-width) serve para centralizar o foco do usuário no fluxo de trabalho.

## Elevation & Depth

A interface é flat-by-default, simulando profundidade através de sombras chapadas (hard shadows), que deslocam o eixo X e Y sem borrar.

### Shadow Vocabulary
- **Flat Hard Shadow** (`4px 4px 0px var(--border-color)`): Sombra básica de cards e componentes maiores.
- **Button Shadow** (`2px 2px 0px var(--border-color)`): Sombra de elementos táteis clicáveis.

## Shapes

Cantos arredondados sutis (`4px` a `12px`), sempre contornados por bordas fortes de `2px solid`.

## Components

### Buttons
- **Shape:** Arredondado sutil (4px)
- **Primary:** Coral com texto Espresso. Hover aumenta a sombra chapada para reforçar a tatilidade.

### Badges
- **Shape:** Arredondado sutil (4px), 1px solid border.
- **Completed:** Fundo Sálvia.
- **Pending:** Fundo Baunilha.
