// Only generates exports matching file names in the components directory
import fs from 'fs';
import path from 'path';

const componentsDir = './components';
const files = fs.readdirSync(componentsDir);

const exports = files
  .filter(file => file.endsWith('.tsx') || file.endsWith('.ts'))
  .filter(file => file !== 'index.ts')
  .map(file => {
    const name = path.basename(file, path.extname(file));
    return `export { ${name} } from '${componentsDir}/${name}';`;
  });

fs.writeFileSync(
  './index.ts',
  exports.join('\n') + '\n'
);
