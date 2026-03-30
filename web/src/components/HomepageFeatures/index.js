import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

const FeatureList = [
  {
    title: 'Easy to Use',
    image: require('@site/static/img/paper.png').default,
    description: (
      <>
        OnyxLib is built with Minecraft fans and developers in mind. OnyxLib and your creations are built with Java and PaperMC.
      </>
    ),
  },
  {
    title: 'Custom Items',
    image: require('@site/static/img/item.png').default,
    description: (
      <>
        Adding custom content like items is a breeze. Registering custom items with textures and a custom name can be done with a single builder method.
      </>
    ),
  },
  {
    title: 'Custom Blocks',
    image: require('@site/static/img/block.png').default,
    description: (
      <>
        Using different techniques and registered events, the illusion of custom blocks is possible with OnyxLib. And it's super easy!
      </>
    ),
  },
];

function Feature({image, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <img src={image} className={styles.featureSvg} alt={title} />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}