const fs = require("fs").promises;
const path = require("path");
const yaml = require("js-yaml");
const ncc = require("@vercel/ncc");

async function build() {
  try {
    console.log("Build javascript action module");

    const actionsDescriptorPath = path.join(process.cwd(), "action.yml");
    const actionsDescriptorString = await fs.readFile(actionsDescriptorPath, {
      encoding: "utf-8",
    });
    const actionsDescriptor = yaml.load(actionsDescriptorString);

    const main = actionsDescriptor.runs.main;
    const post = actionsDescriptor.runs.post;

    const distFolder = path.join(process.cwd(), "dist");
    try {
      await fs.rm(distFolder, {
        recursive: true,
        force: true,
      });
    } catch (e) {
      //ignore
    }

    await fs.mkdir(distFolder);

    if (main) {
      await compile(main);
    }

    if (post) {
      await compile(post);
    }

    process.exit(0);
  } catch (e) {
    console.error(e);
    process.exit(-1);
  }
}

async function compile(name) {
  const sourceFile = path.join(process.cwd(), name);
  const distFolder = path.join(process.cwd(), "dist");
  const outFile = path.join(distFolder, name);
  const backupSourceFile = path.join(distFolder, name.replace(/\.js$/, '.orig.js'));

  console.log(`Compile ${sourceFile} -> ${outFile}`);
  const result = await ncc(sourceFile);

  await fs.copyFile(sourceFile, backupSourceFile);
  await fs.writeFile(outFile, result.code, {
    encoding: "utf-8",
  });
}

build();
