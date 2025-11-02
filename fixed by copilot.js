const { test, expect } = require('@playwright/test');
const { Utility } = require("../../../pages/Utility");

test('Validate Supplier ID Update', async ({ page }) => {
    const objUtility = new Utility();

    await page.goto('https://CA00797-sao-uat.apps.tmpe-clfn-n-06.cf.wellsfargo.net/');
    await expect(page.getByText('Channel Secure Sign On')).toBeVisible();
    await page.locator('#username').click(); // Fixed syntax: changed `page locator` to `page.locator`
    await page.locator('#username').fill('pcontest12');
    await page.locator('#password').click(); // Fixed syntax: changed `page locator` to `page.locator`
    await page.locator('#password').fill('Ok0iRkU84i#L#0UxH4');
    await page.getByText('Sign On', { exact: true }).click();
    await page.locator('label').filter({ hasText: 'SAO IBO' }).click();
    await page.getByRole('button', { name: 'Submit' }).click();
    await page.getByLabel('Supplier Name :').click();
    await page.getByLabel('Supplier Name :').fill('Test');
    await expect(page.getByRole('button', { name: 'Submit' })).toBeVisible();
    await page.getByRole('button', { name: 'Submit' }).click(); // Fixed syntax: corrected method call

    const viewLinks = await page.$$("//tbody[@class='wf-table_data']//tr//span"); // Defined `viewLinks` as an array of elements

    for (const link of viewLinks) {
        const linkName = await link.textContent(); // Fixed syntax: added parentheses for `textContent`
        console.log(linkName);
        await page.getByRole('button', { name: 'View / Edit Details' }).nth(linkName).click(); // Ensure `await` is used

        if (await page.getByLabel('Supplier ID :').isVisible()) { // Fixed syntax: added `await` and used `isVisible`
            await page.getByLabel('Supplier ID :').fill('Test'.concat(objUtility.generateRandomName(5)));
            await page.getByRole('button', { name: 'Submit' }).click();
            break;
        } else {
            await page.getByRole('button', { name: 'Back to Search Results' }).click(); // Fixed syntax: corrected method call
        }
    }
});
