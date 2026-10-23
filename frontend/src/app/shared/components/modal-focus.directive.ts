import { DOCUMENT } from '@angular/common';
import { AfterViewInit, Directive, ElementRef, OnDestroy, inject } from '@angular/core';

/** Mantiene el foco dentro de los diálogos locales y lo devuelve al control que los abrió. */
@Directive({ selector: '.dialog', host: { '(keydown)': 'onKeydown($event)' } })
export class ModalFocusDirective implements AfterViewInit, OnDestroy {
  private readonly host = inject<ElementRef<HTMLElement>>(ElementRef).nativeElement;
  private readonly document = inject(DOCUMENT);
  private readonly previous =
    this.document.activeElement instanceof HTMLElement ? this.document.activeElement : null;
  private readonly selector =
    'button:not([disabled]), input:not([disabled]), select:not([disabled]), textarea:not([disabled]), a[href]';
  ngAfterViewInit() {
    queueMicrotask(() => this.focusables()[0]?.focus());
  }
  ngOnDestroy() {
    queueMicrotask(() => this.previous?.focus());
  }
  onKeydown(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      event.preventDefault();
      const dismiss = Array.from(this.host.querySelectorAll<HTMLButtonElement>('button')).find(
        (button) =>
          button.getAttribute('aria-label') === 'Cerrar' ||
          button.textContent?.trim() === 'Cancelar',
      );
      dismiss?.click();
    }
    if (event.key !== 'Tab') return;
    const items = this.focusables();
    if (!items.length) {
      event.preventDefault();
      return;
    }
    const first = items[0];
    const last = items[items.length - 1];
    if (event.shiftKey && this.document.activeElement === first) {
      event.preventDefault();
      last.focus();
    } else if (!event.shiftKey && this.document.activeElement === last) {
      event.preventDefault();
      first.focus();
    }
  }
  private focusables() {
    return Array.from(this.host.querySelectorAll<HTMLElement>(this.selector));
  }
}
