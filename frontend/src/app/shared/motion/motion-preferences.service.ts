import { DestroyRef, Injectable, inject, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class MotionPreferences {
  readonly reduced = signal(true);
  readonly finePointer = signal(false);
  constructor() {
    const destroy = inject(DestroyRef);
    if (typeof matchMedia === 'undefined') return;
    for (const [query, target] of [
      ['(prefers-reduced-motion: reduce)', this.reduced],
      ['(hover: hover) and (pointer: fine)', this.finePointer],
    ] as const) {
      const media = matchMedia(query);
      const update = () => target.set(media.matches);
      update();
      media.addEventListener('change', update);
      destroy.onDestroy(() => media.removeEventListener('change', update));
    }
  }
}
