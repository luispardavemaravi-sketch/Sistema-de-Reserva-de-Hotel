import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { effect, inject, Injectable, PLATFORM_ID, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly document = inject(DOCUMENT);
  private readonly browser = isPlatformBrowser(inject(PLATFORM_ID));
  readonly dark = signal(
    this.browser ? localStorage.getItem('hotel-parda-theme') !== 'light' : true,
  );

  constructor() {
    this.document.documentElement.classList.toggle('dark', this.dark());
    effect(() => {
      const dark = this.dark();
      this.document.documentElement.classList.toggle('dark', dark);
      this.document.documentElement.style.colorScheme = dark ? 'dark' : 'light';
      if (this.browser) localStorage.setItem('hotel-parda-theme', dark ? 'dark' : 'light');
    });
  }

  toggle() {
    this.dark.update((value) => !value);
  }
}
