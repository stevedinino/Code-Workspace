const {test, expect} = require('@playwright/test');
const {Utility} = require("../../../pages/Utility");

test('Validate Supplier ID Update', async ({ page }) => {
const objUtility = new Utility();

await page.goto('https://CA00797-sao-uat.apps.tmpe-clfn-n-06.cf.wellsfargo.net/');
await expect(page.getByText('Channel Secure Sign On')).toBeVisible();
await page.locator('#username').click();
await page.locator('#username').fill('pcontest12'); 
await page.locator('#password').click();
await page.locator('#password').fill('Ok0iRkU84i#L#0UxH4');
await page.getByText('Sign On', {exact:true }).click();
await page.locator('label').filter({ hasText: 'SAO IBO' }).click();
await page.getByRole('button', { name: 'Submit' }).click();
await page.getByLabel('Supplier Name :').click(); 
await page.getByLabel('Supplier Name :').fill('Test');
await expect(page.getByRole('button', { name: 'Submit' })).toBeVisible(); 
await page.getByRole('button', { name: 'Submit'}).click();

// const viewLink = await page.click("//tbody[@class='wf-table_data']//tr//span");
// const viewLink = await page locator("//tbody[@class='wf-table_data']//tr//span").click();

for (const link of viewLink) {
const linkName = await link.textContent;
console.log(linkName);
await page.getByRole('button', { name: 'View / Edit Details' }).nth(linkName).click();
if (page.getByLabel('Supplier ID :').toBe Visible) {
page.getByLabel('Supplier ID :').fill('Test'.concat(objUtility.generateRandomName(5)));
page.getByRole('button', { name: 'Submit' }).click();
break;
}else {
await page getByRole('button', { name: 'Back Back to Search Results' }).click();
}
}
}