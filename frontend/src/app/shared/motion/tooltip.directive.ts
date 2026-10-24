import { DOCUMENT } from '@angular/common';
import { Directive, ElementRef, DestroyRef, inject, input } from '@angular/core';

let tooltipId = 0;
@Directive({ selector: '[pardaTooltip]', host: {
  '(pointerenter)': 'show()', '(pointerleave)': 'scheduleHide()',
  '(focusin)': 'show()', '(focusout)': 'scheduleHide()',
} })
export class TooltipDirective {
  readonly pardaTooltip = input.required<string>();
  private readonly el = inject<ElementRef<HTMLElement>>(ElementRef).nativeElement;
  private readonly document = inject(DOCUMENT);
  private tip: HTMLDivElement | null = null;
  private timer?: ReturnType<typeof setTimeout>;
  private previous: string | null = null;
  constructor() {
    const escape = (e: KeyboardEvent) => { if (e.key === 'Escape') this.hide(); };
    const hide = () => this.hide();
    this.document.addEventListener('keydown', escape);
    this.document.addEventListener('scroll', hide, true);
    inject(DestroyRef).onDestroy(() => {
      this.hide();
      this.document.removeEventListener('keydown', escape);
      this.document.removeEventListener('scroll', hide, true);
    });
  }
  show() {
    clearTimeout(this.timer);
    if (this.tip || !this.pardaTooltip()) return;
    const tip = this.document.createElement('div');
    tip.className = 'parda-tooltip';
    tip.id = `parda-tooltip-${++tooltipId}`;
    tip.setAttribute('role', 'tooltip');
    tip.textContent = this.pardaTooltip();
    tip.addEventListener('pointerenter', () => clearTimeout(this.timer));
    tip.addEventListener('pointerleave', () => this.scheduleHide());
    this.document.body.append(tip);
    const rect = this.el.getBoundingClientRect();
    const box = tip.getBoundingClientRect();
    const width = this.document.documentElement.clientWidth;
    const height = this.document.documentElement.clientHeight;
    tip.style.left = `${Math.max(8, Math.min(width - box.width - 8, rect.left + (rect.width - box.width) / 2))}px`;
    tip.style.top = `${Math.max(8, Math.min(height - box.height - 8, rect.top > box.height + 16 ? rect.top - box.height - 10 : rect.bottom + 10))}px`;
    this.previous = this.el.getAttribute('aria-describedby');
    this.el.setAttribute('aria-describedby', [this.previous, tip.id].filter(Boolean).join(' '));
    this.tip = tip;
  }
  scheduleHide() { this.timer = setTimeout(() => this.hide(), 120); }
  private hide() {
    clearTimeout(this.timer);
    if (!this.tip) return;
    this.tip.remove(); this.tip = null;
    if (this.previous) this.el.setAttribute('aria-describedby', this.previous);
    else this.el.removeAttribute('aria-describedby');
  }
}
