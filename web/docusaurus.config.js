// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config

import {themes as prismThemes} from 'prism-react-renderer';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'OnyxLib',
  tagline: 'Minecraft Server-Side Content Creation Framework',
  favicon: 'img/favicon.ico',
  future: { v4: true },
  url: 'https://onyx.zombienw.com',
  baseUrl: '/',
  organizationName: 'ZombieNW',
  projectName: 'OnyxLib',
  onBrokenLinks: 'throw',
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: './sidebars.js',
        },
        blog: {
          showReadingTime: true,
          feedOptions: {
            type: ['rss', 'atom'],
            xslt: true,
          },
          onInlineTags: 'warn',
          onInlineAuthors: 'warn',
          onUntruncatedBlogPosts: 'warn',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: 'img/docusaurus-social-card.jpg',
      colorMode: {
        respectPrefersColorScheme: true,
      },
      navbar: {
        title: 'OnyxLib',
        logo: {
          alt: 'OnyxLib Logo',
          src: 'img/logo.png',
        },
        items: [
            {
            type: 'docSidebar',
            sidebarId: 'introSidebar',
            position: 'left',
            label: 'Getting Started',
          },
          {
            to: 'pathname:///javadocs/index.html', 
            label: 'JavaDocs',
            position: 'left',
        },
          {
            href: 'https://github.com/ZombieNW/OnyxLib',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Docs',
            items: [
              {
                label: 'Getting Started',
                to: '/docs/Getting Started',
              },
            ],
          },
          {
            title: 'Community',
            items: [
              {
                label: 'ZombieNW',
                href: 'https://zombienw.com/',
              },
              {
                label: 'Discord',
                href: 'https://discord.gg/tKFMy85',
              },
            ],
          },
          {
            title: 'More',
            items: [
              {
                label: 'GitHub',
                href: 'https://github.com/ZombieNW/OnyxLib',
              },
            ],
          },
        ],
        copyright: `Made with ❤️ by ZombieNW ${new Date().getFullYear()}. Built with Docusaurus.`,
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
      },
    }),
};

export default config;
