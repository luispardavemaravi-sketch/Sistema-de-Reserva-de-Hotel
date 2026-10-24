import { Directive, ElementRef, effect, inject, input } from '@angular/core';
import { MotionPreferences } from './motion-preferences.service';

@Directive({ selector: '[pardaStatus]' })
export class StatusPulseDirective {
  readonly pardaStatus = input.required<string>();
  private readonly el = inject<ElementRef<HTMLElement>>(ElementRef).nativeElement;
  private readonly motion = inject(MotionPreferences);
  constructor() {
    let previous: string | undefined;
    effect(onCleanup => {
      const current = this.pardaStatus();
      if (previous !== undefined && current !== previous && !this.motion.reduced() && this.el.animate) {
        const animation = this.el.animate([{ opacity: .4 }, { opacity: 1 }], { duration: 220, easing: 'ease-out' });
        onCleanup(() => animation.cancel());
      }
      previous = current;
    });
  }
}
